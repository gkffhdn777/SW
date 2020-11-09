package com.kao.domain;

import com.kao.event.RawEvent;

public class Bank {

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
