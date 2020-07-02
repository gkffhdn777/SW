package com.kakaobank.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaobank.event.RawEvent;
import com.kakaobank.event.BankActionType;

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
		this.bankProducer.send(new RawEvent(BankActionType.REGISTRATION, this));
	}

	public Registration createAccount() {
		if (id == null) {
			throw new RuntimeException("회원 가입이 필요 합니다.");
		}
		this.account = new Account(id, bankProducer);
		return this;
	}

	public AccountNumber getAccountNumber() {
		if (account == null) {
			throw new RuntimeException("계좌를 개설해 주세요.");
		}
		return account.getAccountNumber();
	}

	public CustomerId getId() {
		if (id == null) {
			throw new RuntimeException("해당고객은 존재 하지않습니다.");
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
