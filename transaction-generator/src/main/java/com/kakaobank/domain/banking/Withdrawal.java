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

public final class Withdrawal implements BankService {

	@JsonProperty("customerId")
	private CustomerId id;

	@JsonProperty("accountNumber")
	private AccountNumber accountNumber;

	@JsonProperty("money")
	private Money money;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	public Withdrawal(CustomerId id, AccountNumber accountNumber, Money money) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.money = money;
		this.createDate = LocalDateTime.now();
	}

	@Override
	public Money useOfMoney(CustomerBalance customerBalance, BankProducer<RawEvent> bankProducer) {
		Money resultMoney = customerBalance.minus(id, accountNumber, money);
		bankProducer.send(id.getId(), new RawEvent(BankActionType.WITHDRAWAL, this));
		return resultMoney;
	}
}
