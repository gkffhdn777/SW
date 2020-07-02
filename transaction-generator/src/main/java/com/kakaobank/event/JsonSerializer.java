package com.kakaobank.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.errors.SerializationException;

import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer implements Serializer<RawEvent> {

	@Override
	public byte[] serialize(final String topic, final RawEvent rawEvent) {
		if (rawEvent == null) {
			throw new NullPointerException("RawEvent cannot be null.");
		}
		try {
			return JsonMapper.getObjectMapper().writeValueAsBytes(rawEvent);
		} catch (JsonProcessingException ex) {
			throw new SerializationException("Error JsonSerializer event message", ex);
		}
	}
}
