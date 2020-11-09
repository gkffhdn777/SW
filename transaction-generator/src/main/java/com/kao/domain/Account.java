package com.kao.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kao.event.RawEvent;
import com.kao.event.BankActionType;

public final class Account {

	@JsonProperty("customerId")
	private CustomerId id;

	@JsonProperty("accountNumber")
	private AccountNumber accountNumber;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	public Account(final CustomerId id, final BankProducer<RawEvent> bankProducer) {
		verifyCustomerId(id);
		this.accountNumber = accountNumber.create();
		this.createDate = LocalDateTime.now();
		if (bankProducer == null) {
			throw new NullPointerException("BankProducer cannot be null.");
		}
		bankProducer.send(id.getId(), new RawEvent(BankActionType.ACCOUNT, this));
	}

	private void verifyCustomerId(final CustomerId id) {
		if (id == null) {
			throw new IllegalArgumentException("CustomerId cannot be null.");
		}
		this.id = id;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", accountNumber=" + accountNumber +
				", createDate=" + createDate +
				'}';
	}
}
