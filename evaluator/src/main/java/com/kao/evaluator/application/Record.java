package com.kao.evaluator.application;

import com.kao.evaluator.event.RawEvent;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.record.TimestampType;

public final class Record {

	private final String topic;

	private final int partition;

	private final long offset;

	private final long timestamp;

	private final TimestampType timestampType;

	private final int serializedKeySize;

	private final int serializedValueSize;

	private final Headers headers;

	private final String key;

	private final RawEvent value;

	private Record(
			String topic,
			int partition,
			long offset,
			long timestamp,
			TimestampType timestampType,
			int serializedKeySize,
			int serializedValueSize,
			Headers headers, String key, RawEvent value) {
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.timestamp = timestamp;
		this.timestampType = timestampType;
		this.serializedKeySize = serializedKeySize;
		this.serializedValueSize = serializedValueSize;
		this.headers = headers;
		this.key = key;
		this.value = value;
	}

	public String getTopic() {
		return topic;
	}

	public int getPartition() {
		return partition;
	}

	public long getOffset() {
		return offset;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public TimestampType getTimestampType() {
		return timestampType;
	}

	public int getSerializedKeySize() {
		return serializedKeySize;
	}

	public int getSerializedValueSize() {
		return serializedValueSize;
	}

	public Headers getHeaders() {
		return headers;
	}

	public String getKey() {
		return key;
	}

	public RawEvent getValue() {
		return value;
	}

	public static final class ConsumerRecordBuilder {
		private String topic;

		private int partition;

		private long offset;

		private long timestamp;

		private TimestampType timestampType;

		private int serializedKeySize;

		private int serializedValueSize;

		private Headers headers;

		private String key;

		private RawEvent value;

		private ConsumerRecordBuilder(String topic, String key, int partition, long offset, RawEvent value) {
			this.topic = topic;
			this.key = key;
			this.partition = partition;
			this.offset = offset;
			this.value = value;
		}

		public static ConsumerRecordBuilder aConsumerRecord(String topic, String key, int partition, long offset, RawEvent value) {
			return new ConsumerRecordBuilder(topic, key, partition, offset, value);
		}

		public ConsumerRecordBuilder withTimestamp(long timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public ConsumerRecordBuilder withTimestampType(TimestampType timestampType) {
			this.timestampType = timestampType;
			return this;
		}

		public ConsumerRecordBuilder withSerializedKeySize(int serializedKeySize) {
			this.serializedKeySize = serializedKeySize;
			return this;
		}

		public ConsumerRecordBuilder withSerializedValueSize(int serializedValueSize) {
			this.serializedValueSize = serializedValueSize;
			return this;
		}

		public ConsumerRecordBuilder withHeaders(Headers headers) {
			this.headers = headers;
			return this;
		}

		public Record build() {
			return new Record(topic, partition, offset, timestamp, timestampType, serializedKeySize, serializedValueSize, headers, key, value);
		}
	}
}
