package com.kao.domain;

import com.kao.event.RawEvent;

public interface BankProducer<T extends RawEvent> {
	void send(String key, T rawEvent);
}
