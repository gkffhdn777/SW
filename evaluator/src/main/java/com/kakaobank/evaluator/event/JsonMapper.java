package com.kakaobank.evaluator.event;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	private JsonMapper() {
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
	}

	private static final class LazyHolder {
		public static final JsonMapper INSTANCE = new JsonMapper();
	}

	public static JsonMapper getInstance() {
		return JsonMapper.LazyHolder.INSTANCE;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public <T> String converterToString(T event) {
		if (event == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(event);
		} catch (JsonProcessingException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new JsonException("Error occurred while converting the message to a string.");
		}
	}

	public <T> T converterToEventMessage(String payload, Class<T> clazz) {
		if (payload == null) {
			return null;
		}
		try {
			return objectMapper.readValue(payload, clazz);
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new JsonException("Error occurred while converting the message to a string.");
		}
	}
}
