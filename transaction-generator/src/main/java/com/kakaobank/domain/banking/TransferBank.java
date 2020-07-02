package com.kakaobank.domain.banking;

import com.kakaobank.domain.Money;

public interface TransferBank {
	Boolean send(String receiverBank, String receiverAccountNumber, String receiverName, Money money);
}
