package com.kao.infra;

import java.util.Properties;

import com.kao.domain.BankProducer;
import com.kao.event.JsonSerializer;
import com.kao.event.RawEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GeneratorProducer implements BankProducer<RawEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorProducer.class);

	private static final String TO_PIC = "fds.transactions";

	@Override
	public void send(final String key, final RawEvent rawEvent) {
		final KafkaProducer<String, RawEvent> producer = createProducer();
		producer.initTransactions();

		try {
			ProducerRecord<String, RawEvent> producerRecord =
					new ProducerRecord<>(TO_PIC, key, rawEvent);

			producer.beginTransaction();

			
			producer.send(producerRecord, (metadata, exception) -> {

			});
			producer.commitTransaction();

		} catch (KafkaException ex) {
			LOGGER.error(ex.getMessage(), ex);
			producer.abortTransaction();
		} finally {
			producer.close();
		}
	}

	private static KafkaProducer<String, RawEvent> createProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
		properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "bank.transactions");
		properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10000);
		return new KafkaProducer<>(properties);
	}
}