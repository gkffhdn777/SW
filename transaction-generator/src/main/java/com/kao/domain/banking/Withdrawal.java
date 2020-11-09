package com.kao.domain.banking;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kao.domain.AccountNumber;
import com.kao.domain.BankService;
import com.kao.domain.CustomerBalance;
import com.kao.domain.CustomerId;
import com.kao.domain.Money;
import com.kao.domain.BankProducer;
import com.kao.event.RawEvent;
import com.kao.event.BankActionType;

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
