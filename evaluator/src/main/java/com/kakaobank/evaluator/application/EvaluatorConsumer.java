package com.kakaobank.evaluator.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

import com.kakaobank.evaluator.event.BankActionType;
import com.kakaobank.evaluator.event.JsonDeserializer;
import com.kakaobank.evaluator.event.RawEvent;
import com.kakaobank.evaluator.infra.CrudRepository;
import com.kakaobank.evaluator.store.EventStoreException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "evaluator-group");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));

		try (KafkaConsumer<String, RawEvent> consumer = new KafkaConsumer<>(properties)) {
			consumer.subscribe(Collections.singletonList(TO_PIC));
			while (true) {
				ConsumerRecords<String, RawEvent> records = consumer.poll(Duration.ofMillis(5));

				for (ConsumerRecord<String, RawEvent> record : records) {
					try {
						Event event = record.value().getBankActionType().getEvent(record.value().getPayload());

						if (record.value().getBankActionType() == BankActionType.WITHDRAWAL
								|| record.value().getBankActionType() == BankActionType.TRANSFER) {
							LOGGER.info("이체 및 인출 감지 시작 시간 {}, event : {}", LocalDateTime.now(), event);
							evaluatorService.anomalyDetection(event);
						}

						if (!sendEvent(event)) {
							throw new EventStoreException("Event storage failed.");
						}
						consumer.commitSync();

					} catch (Exception ex) {
						saveRecord(record);
					}
				}
			}
		}
	}

	private Boolean sendEvent(final Event event) {
		return eventHandler.apply(event);
	}

	private void saveRecord(final ConsumerRecord<String, RawEvent> record) {
		final Record buildRecord = Record.ConsumerRecordBuilder.aConsumerRecord(
				record.topic(),
				record.key(),
				record.partition(),
				record.offset(),
				record.value()).build();

		CrudRepository.getInstance().save(buildRecord, Record.class);
	}
}
