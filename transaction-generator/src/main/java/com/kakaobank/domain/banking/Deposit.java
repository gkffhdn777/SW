package com.kakaobank.domain.banking;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.kakaobank.domain.AccountNumber;
import com.kakaobank.domain.BankService;
import com.kakaobank.domain.CustomerBalance;
import com.kakaobank.domain.CustomerId;
import com.kakaobank.domain.Money;
import com.kakaobank.domain.BankProducer;

import com.kakaobank.event.RawEvent;
import com.kakaobank.event.BankActionType;

public final class Deposit implements BankService {

	@JsonProperty("customerId")
	private CustomerId id;

	@JsonProperty("accountNumber")
	private AccountNumber accountNumber;

	@JsonProperty("money")
	private Money money;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	public Deposit(final CustomerId id, final AccountNumber accountNumber, final Money money) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.money = money;
		this.createDate = LocalDateTime.now();
	}

	@Override
	public Money useOfMoney(final CustomerBalance customerBalance, final BankProducer<RawEvent> bankProducer) {
		Money resultMoney = customerBalance.plus(id, accountNumber, money);
		bankProducer.send(id.getId(), new RawEvent(BankActionType.DEPOSIT, this));
		return resultMoney;
	}
}
