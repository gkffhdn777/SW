package com.kakaobank.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaobank.event.RawEvent;
import com.kakaobank.event.BankActionType;

public final class Account {

	@JsonProperty("customerId")
	private CustomerId id;

	@JsonProperty("accountNumber")
	private AccountNumber accountNumber;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	public Account(final CustomerId id, final BankProducer<RawEvent> bankProducer) {
		this.id = id;
		this.accountNumber = accountNumber.create();
		this.createDate = LocalDateTime.now();
		bankProducer.send(new RawEvent(BankActionType.ACCOUNT, this));
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
