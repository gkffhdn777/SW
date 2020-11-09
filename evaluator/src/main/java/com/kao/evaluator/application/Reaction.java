package com.kao.evaluator.application;

import com.kao.evaluator.event.AccountNumber;
import com.kao.evaluator.event.Money;

public interface Reaction extends Event {
	Money getMoney();

	AccountNumber getAccountNumber();
}
