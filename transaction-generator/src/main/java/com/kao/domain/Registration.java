package com.kao.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kao.event.RawEvent;
import com.kao.event.BankActionType;

public final class Registration {
	@JsonProperty("customerId")
	private final CustomerId id;

	@JsonProperty("name")
	private final String name;

	@JsonProperty("birthDate")
	private final LocalDate birthDate;

	@JsonProperty("createDate")
	private final LocalDateTime createDate;

	private Account account;

	private final BankProducer<RawEvent> bankProducer;

	public Registration(final Customer customer, final BankProducer<RawEvent> bankProducer) {
		this.id = CustomerId.createUUID();
		this.name = validationName(customer.getName());
		this.birthDate = customer.getBirthDate();
		this.createDate = LocalDateTime.now();
		this.bankProducer = bankProducer;
		this.bankProducer.send(id.getId(), new RawEvent(BankActionType.REGISTRATION, this));
	}

	public Registration createAccount() {
		if (id == null) {
			throw new RuntimeException("Membership is required.");
		}
		this.account = new Account(id, bankProducer);
		return this;
	}

	public AccountNumber getAccountNumber() {
		if (account == null) {
			throw new RuntimeException("Open a bank account.");
		}
		return account.getAccountNumber();
	}

	public CustomerId getId() {
		if (id == null) {
			throw new RuntimeException("The customer does not exist.");
		}
		return id;
	}

	public static String validationName(String name) {
		if (name == null || isNumeric(name) || name.length() <= 1) {
			throw new IllegalArgumentException("The name is wrong.");
		}
		return name;
	}

	public static boolean isNumeric(String s) {
		return s.chars().anyMatch(Character::isDigit);
	}
}
