package com.kakaobank.evaluator.store;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import com.kakaobank.evaluator.application.EventHandler;
import com.kakaobank.evaluator.application.Event;

public final class EventStoreHandler implements EventHandler {

	private static final String EVENT_METHOD = "execute";

	private static final String CLASS_NAME = "com.kakaobank.evaluator.store.EventStore";

	@Override
	public Boolean apply(final Event e) {
		Event event = Objects.requireNonNull(e, "Event message cannot be null.");
		try {
			Class<?> eventClass = Class.forName(CLASS_NAME);
			Constructor<?> constructor = eventClass.getDeclaredConstructor();
			constructor.setAccessible(true);

			Method method = eventClass
					.getDeclaredMethod(EVENT_METHOD, event.getClass());

			method.setAccessible(true);
			return (Boolean) method.invoke(constructor.newInstance(), event);

		} catch (InvocationTargetException | NoSuchMethodException |
				IllegalAccessException | ClassNotFoundException |
				InstantiationException ex) {
			throw new EventStoreException("An error occurred while sending an event message to the method to be executed.", ex);
		}
	}
}
