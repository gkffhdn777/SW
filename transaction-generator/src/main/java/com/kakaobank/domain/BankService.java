package com.kakaobank.domain;

import com.kakaobank.event.RawEvent;

public interface BankService {
	Money useOfMoney(CustomerBalance customerBalance, BankProducer<RawEvent> bankProducer);
}
