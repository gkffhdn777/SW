package com.kao.domain;

import com.kao.event.RawEvent;

public interface BankService {
	Money useOfMoney(CustomerBalance customerBalance, BankProducer<RawEvent> bankProducer);
}
