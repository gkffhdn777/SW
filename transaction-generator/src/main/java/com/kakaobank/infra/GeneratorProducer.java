package com.kakaobank.infra;

import java.util.Properties;

import com.kakaobank.domain.BankProducer;
import com.kakaobank.event.JsonSerializer;
import com.kakaobank.event.RawEvent;
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
	public void send(final RawEvent rawEvent) {
		final KafkaProducer<String, RawEvent> producer = createProducer();
		producer.initTransactions();

		try {
			ProducerRecord<String, RawEvent> producerRecord =
					new ProducerRecord<>(TO_PIC, 0, null, rawEvent);

			producer.beginTransaction();
			producer.send(producerRecord);
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
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
		properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "bank.transactions");
		properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10000);
		return new KafkaProducer<>(properties);
	}
}

//./kafka-topics.sh --create --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --replication-factor 3 --partitions 0 --topic fds.transactions
// ./kafka-console-consumer.sh --topic fds.transactions --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --from-beginning
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --group ncommunity-migration-master --describe
//
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --group ncommunity-migration-slaves --describe
//
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --group ncommunity-event --describe
//
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --group ncommunity-migration-search --describe
//
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --list
//
//./kafka-topics.sh --delete --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --topic migration-slaves
//
//./kafka-topics.sh --delete --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --topic migration-master
//
//./kafka-topics.sh --delete --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --topic migration-search
//
//./kafka-topics.sh --list --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181
//
//./kafka-topics.sh --create --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --replication-factor 1 --partitions 2 --topic migration-slaves
//
//./kafka-topics.sh --create --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --replication-factor 1 --partitions 2 --topic migration-master
//
//./kafka-topics.sh --create --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --replication-factor 2 --partitions 5 --topic migration-search
//
//./kafka-delete-records.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --offset-json-file j.json
//
//./kafka-consumer-groups.sh --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --group ncommunity-migration-search --topic migration-search --reset-offsets --to-offset 8790 --execute