package com.kakaobank.evaluator.application;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

public final class EvaluatorProducer {

	private static final String TO_PIC = "fds.detections";

	//./kafka-console-consumer.sh --topic fds.detections --bootstrap-server 172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092 --from-beginning
	//./kafka-topics.sh --create --zookeeper 172.19.136.232:2181,172.19.136.233:2181,172.19.136.234:2181 --replication-factor 3 --partitions 1 --topic fds.detections
	public CompletableFuture<Future<RecordMetadata>> send(final String message) {
		try (final KafkaProducer<String, String> producer = createProducer()) {
			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TO_PIC, 0, null, message);
			Future<RecordMetadata> future = producer.send(producerRecord);
			return CompletableFuture.supplyAsync(() -> future);
		} catch (KafkaException ex) {
			throw new KafkaProducerException(ex);
		}
	}

	public static KafkaProducer<String, String> createProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		return new KafkaProducer<>(properties);
	}
}
