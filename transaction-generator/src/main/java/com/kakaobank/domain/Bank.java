package com.kakaobank.domain;

import com.kakaobank.event.RawEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bank {

	private static final Logger LOGGER = LoggerFactory.getLogger(Bank.class);

	private final CustomerBalance repository;

	private final BankProducer<RawEvent> bankProducer;

	public Bank(final CustomerBalance repository, final BankProducer<RawEvent> bankProducer) {
		this.repository = repository;
		this.bankProducer = bankProducer;
	}

	public Money requestToBank(final BankService bankService) {
		if (bankService == null) {
			throw new NullPointerException("BankService cannot be null.");
		}
		return bankService.useOfMoney(this.repository, bankProducer);
	}
}
