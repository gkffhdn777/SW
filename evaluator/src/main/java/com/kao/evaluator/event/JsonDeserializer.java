package com.kao.evaluator.event;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonDeserializer implements Deserializer<RawEvent> {

	@Override
	public RawEvent deserialize(String topic, byte[] data) {
		try {
			return JsonMapper.getInstance().getObjectMapper().readValue(data, RawEvent.class);
		} catch (IOException ex) {
			throw new SerializationException("Error deserialize raw event message");
		}
	}
}
