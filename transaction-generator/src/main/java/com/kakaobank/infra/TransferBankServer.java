package com.kakaobank.infra;

import com.kakaobank.domain.Money;
import com.kakaobank.domain.banking.TransferBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TransferBankServer implements TransferBank {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferBankServer.class);

	@Override
	public Boolean send(final String receiverBank, final String receiverAccountNumber, final String receiverName, final Money money) {
		try {
			LOGGER.debug("Transfer is complete. receiverBank : {}, receiverAccountNumber : {}, receiverName : {}, money : {} ",
					receiverBank, receiverAccountNumber, receiverName, money);
			return true;
		} catch (Exception e) {
			LOGGER.error("Error during transfer. receiverBank : {}, receiverAccountNumber : {}, receiverName : {}, money : {} ",
					receiverBank, receiverAccountNumber, receiverName, money);
			return false;
		}
	}
}
