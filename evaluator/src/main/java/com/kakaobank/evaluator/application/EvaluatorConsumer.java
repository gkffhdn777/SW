package com.kakaobank.evaluator.application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import com.kakaobank.evaluator.event.BankActionType;
import com.kakaobank.evaluator.event.RawEvent;
import com.kakaobank.evaluator.infra.CrudRepository;
import com.kakaobank.evaluator.store.EventStoreException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EvaluatorConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluatorConsumer.class);

	private static final String TO_PIC = "fds.transactions";

	private final EvaluatorService evaluatorService;

	private final EventHandler eventHandler;

	private final KafkaConsumer<String, RawEvent> consumer;

	public EvaluatorConsumer(
			final EvaluatorService evaluatorService,
			final EventHandler eventHandler,
			final KafkaConsumer<String, RawEvent> consumer) {
		this.evaluatorService = evaluatorService;
		this.eventHandler = eventHandler;
		this.consumer = consumer;
	}

	public void start() {
		try {
			consumer.subscribe(Collections.singletonList(TO_PIC));
			while (true) {
				ConsumerRecords<String, RawEvent> records = consumer.poll(Duration.ofMillis(1000));
				for (ConsumerRecord<String, RawEvent> record : records) {
						Event event = record.value().getBankActionType().getEvent(record.value().getPayload());
						detection(record);
						if (!sendEvent(event)) {
							//이벤트 저장 실패시 인메모리 재처리가 필요 함.
							throw new EventStoreException("Event storage failed.");
						}
						consumer.commitSync();
				}
			}
		} catch (Exception ex) {
			throw new KafkaConsumerException(ex);
		} finally {
			consumer.close();
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

	private void detection(final ConsumerRecord<String, RawEvent> record) {
		Event event = record.value().getBankActionType().getEvent(record.value().getPayload());
		try {
			if (record.value().getBankActionType() == BankActionType.WITHDRAWAL
					|| record.value().getBankActionType() == BankActionType.TRANSFER) {
				LOGGER.info("이체 및 인출 감지 시작 시간 {}, event : {}", LocalDateTime.now(), event);
				evaluatorService.anomalyDetection(event);
			}
		} catch (Exception ex) {
			saveRecord(record);
			LOGGER.error(ex.getMessage(), ex);
		}

	}
}
