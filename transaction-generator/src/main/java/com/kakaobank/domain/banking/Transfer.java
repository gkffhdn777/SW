package com.kakaobank.domain.banking;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaobank.domain.AccountNumber;
import com.kakaobank.domain.BankService;
import com.kakaobank.domain.CustomerBalance;
import com.kakaobank.domain.CustomerId;
import com.kakaobank.domain.Money;
import com.kakaobank.domain.BankProducer;
import com.kakaobank.infra.TransferBankServer;
import com.kakaobank.event.RawEvent;
import com.kakaobank.event.BankActionType;
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
			Boolean sent = bank.send(ReceiverBank.KBBANK.name(), receiverAccountNumber, receiverName, money);
			if (!sent) {
				transferMoney = customerBalance.plus(id, accountNumber, money);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new TransferException("Error remittance.");
		}
		bankProducer.send(new RawEvent(BankActionType.TRANSFER, this));
		return transferMoney;
	}
}
