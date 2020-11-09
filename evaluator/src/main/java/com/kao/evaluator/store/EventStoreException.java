package com.kao.evaluator.store;

public class EventStoreException extends RuntimeException {
	public EventStoreException(String message) {
		super(message);
	}

	public EventStoreException(String message, Throwable cause) {
		super(message, cause);
	}
}
