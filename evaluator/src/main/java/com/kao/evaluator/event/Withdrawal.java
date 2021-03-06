package com.kao.evaluator.event;

import java.time.LocalDateTime;

import com.kao.evaluator.application.Reaction;

public final class Withdrawal implements Reaction {

	private CustomerId customerId;

	private AccountNumber accountNumber;

	private Money money;

	private LocalDateTime createDate;

	@Override
	public CustomerId getCustomerId() {
		return customerId;
	}

	@Override
	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	@Override
	public Money getMoney() {
		return money;
	}

	@Override
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
	}

	public void setAccountNumber(AccountNumber accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Withdrawal{" +
				"customerId=" + customerId +
				", accountNumber=" + accountNumber +
				", money=" + money +
				", createDate=" + createDate +
				'}';
	}
}
