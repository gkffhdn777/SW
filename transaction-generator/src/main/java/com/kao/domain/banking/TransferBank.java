package com.kao.domain.banking;

import com.kao.domain.Money;

public interface TransferBank {
	Boolean send(String receiverBank, String receiverAccountNumber, String receiverName, Money money);
}
