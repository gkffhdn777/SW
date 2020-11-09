package com.kao.domain;

public interface CustomerBalance {
	Money plus(CustomerId id, AccountNumber no, Money money);

	Money minus(CustomerId id, AccountNumber accountNumber, Money money);

	Money findByCustomerId(CustomerId id, AccountNumber accountNumber);
}
