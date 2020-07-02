package com.kakaobank.evaluator.application;

import java.time.Duration;

import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.kakaobank.evaluator.event.BankActionType;
import com.kakaobank.evaluator.event.JsonDeserializer;
import com.kakaobank.evaluator.event.RawEvent;
import com.kakaobank.evaluator.infra.CrudRepository;

import com.kakaobank.evaluator.store.EventStoreException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EvaluatorConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluatorConsumer.class);

	private static final String TO_PIC = "fds.transactions";

	private final EvaluatorService evaluatorService;

	private final EventHandler eventHandler;

	public EvaluatorConsumer(final EvaluatorService evaluatorService, final EventHandler eventHandler) {
		this.evaluatorService = evaluatorService;
		this.eventHandler = eventHandler;
	}

	public void start() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "evaluator-group");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));

		KafkaConsumer<String, RawEvent> consumer = new KafkaConsumer<>(properties);
		consumer.subscribe(Collections.singletonList(TO_PIC));

		try {
			AtomicInteger atomic = new AtomicInteger(0);

			while (true) {
				ConsumerRecords<String, RawEvent> records = consumer.poll(Duration.ofMillis(5));
				for (ConsumerRecord<String, RawEvent> record : records) {

					Event event = record.value().getBankActionType().getEvent(record.value().getPayload());

					if (record.value().getBankActionType() == BankActionType.WITHDRAWAL
							|| record.value().getBankActionType() == BankActionType.TRANSFER) {

						consume(event).exceptionally(ex -> {
							saveRecord(record);
							throw new KafkaConsumerException(ex);
						}).isCompletedExceptionally();
					}

					Boolean isSend = sendEvent(event);
					if (!isSend) {
						throw new EventStoreException("Event storage failed.");
					}

					consumer.commitAsync((offsets, ex) -> {
						int marker = atomic.incrementAndGet();
						if (ex != null) {
							LOGGER.error("Error Kafka commitAsync : {}", ex.getMessage());
							if (marker == atomic.get()) {
								consumer.commitAsync();
							}
						}
					});
				}
			}

		} catch (Exception ex) {
			LOGGER.error("Error Kafka consumer : {}", ex.getMessage());
		} finally {
			try {
				consumer.commitSync();
			} finally {
				consumer.close();
			}
		}
	}

	private CompletableFuture<Future<RecordMetadata>> consume(final Event event) {
		return evaluatorService.anomalyDetection(event);
	}

	private Boolean sendEvent(final Event event) {
		return eventHandler.apply(event);
	}

	private static void saveRecord(final ConsumerRecord<String, RawEvent> record) {
		final Record buildRecord = Record.ConsumerRecordBuilder.aConsumerRecord(
				record.topic(),
				record.key(),
				record.partition(),
				record.offset(),
				record.value()).build();

		CrudRepository.getInstance().save(buildRecord, Record.class);
	}
}
