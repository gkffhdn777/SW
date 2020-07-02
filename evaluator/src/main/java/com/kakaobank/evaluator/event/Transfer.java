package com.kakaobank.evaluator.event;

import java.time.LocalDateTime;

import com.kakaobank.evaluator.application.Reaction;

public final class Transfer implements Reaction {

	private CustomerId customerId;

	private AccountNumber accountNumber;

	private ReceiverBank receiverBank;

	private String receiverAccountNumber;

	private String receiverName;

	private Money money;

	private LocalDateTime createDate;

	@Override
	public CustomerId getCustomerId() {
		return customerId;
	}

	public AccountNumber getAccountNumber() {
		return accountNumber;
	}

	public ReceiverBank getReceiverBank() {
		return receiverBank;
	}

	public String getReceiverAccountNumber() {
		return receiverAccountNumber;
	}

	public String getReceiverName() {
		return receiverName;
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

	public void setReceiverBank(ReceiverBank receiverBank) {
		this.receiverBank = receiverBank;
	}

	public void setReceiverAccountNumber(String receiverAccountNumber) {
		this.receiverAccountNumber = receiverAccountNumber;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Transfer{" +
				"customerId=" + customerId +
				", accountNumber=" + accountNumber +
				", receiverBank=" + receiverBank +
				", receiverAccountNumber='" + receiverAccountNumber + '\'' +
				", receiverName='" + receiverName + '\'' +
				", money=" + money +
				", createDate=" + createDate +
				'}';
	}
}
