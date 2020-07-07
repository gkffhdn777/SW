package com.kakaobank.domain;

import com.kakaobank.event.RawEvent;

public interface BankProducer<T extends RawEvent> {
	void send(String key, T rawEvent);
}
