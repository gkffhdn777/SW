package com.kao.evaluator.event;

import java.time.LocalDateTime;

import com.kao.evaluator.application.Event;

public final class Account implements Event {

	private CustomerId customerId;

	private AccountNumber accountNumber;

	private LocalDateTime createDate;

	@Override
	public CustomerId getCustomerId() {
		return customerId;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
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

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Account{" +
				"customerId=" + customerId +
				", accountNumber=" + accountNumber +
				", createDate=" + createDate +
				'}';
	}
}
