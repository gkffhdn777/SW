package com.kao.domain.banking;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kao.domain.AccountNumber;
import com.kao.domain.BankService;
import com.kao.domain.CustomerBalance;
import com.kao.domain.CustomerId;
import com.kao.domain.Money;
import com.kao.domain.BankProducer;
import com.kao.infra.TransferBankServer;
import com.kao.event.RawEvent;
import com.kao.event.BankActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Transfer implements BankService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Transfer.class);

	@JsonProperty("customerId")
	private CustomerId id;

	@JsonProperty("accountNumber")
	private AccountNumber accountNumber;

	@JsonProperty("receiverBank")
	private ReceiverBank receiverBank;

	@JsonProperty("receiverAccountNumber")
	private String receiverAccountNumber;

	@JsonProperty("receiverName")
	private String receiverName;

	@JsonProperty("money")
	private Money money;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	private TransferBank bank = new TransferBankServer();

	public Transfer(final CustomerId id,
			final AccountNumber accountNumber,
			final ReceiverBank receiverBank,
			final String receiverAccountNumber,
			final String receiverName,
			final Money money) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.receiverBank = receiverBank;
		this.receiverAccountNumber = receiverAccountNumber;
		this.receiverName = receiverName;
		this.money = money;
		this.createDate = LocalDateTime.now();
	}

	@Override
	public Money useOfMoney(final CustomerBalance customerBalance, final BankProducer<RawEvent> bankProducer) {
		Money transferMoney;
		try {
			transferMoney = customerBalance.minus(id, accountNumber, money);
			Boolean isSent = bank.send(ReceiverBank.KBBANK.name(), receiverAccountNumber, receiverName, money);
			if (!isSent) {
				transferMoney = customerBalance.plus(id, accountNumber, money);
			}
		} catch (Exception ex) {
			throw new TransferException("Error remittance.", ex);
		}
		bankProducer.send(id.getId(), new RawEvent(BankActionType.TRANSFER, this));
		return transferMoney;
	}
}
