package com.kao.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonMapper.class);

	private static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
		OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		OBJECT_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	private JsonMapper() {
	}

	public static <T> String writeValueAsString(final T event) {
		if (event == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.writeValueAsString(event);
		} catch (JsonProcessingException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new JsonException("Error occurred while converting the message to a string.");
		}
	}

}
