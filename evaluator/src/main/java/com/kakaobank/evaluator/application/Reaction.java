package com.kakaobank.evaluator.application;

import com.kakaobank.evaluator.event.AccountNumber;
import com.kakaobank.evaluator.event.Money;

public interface Reaction extends Event {
	Money getMoney();

	AccountNumber getAccountNumber();
}
