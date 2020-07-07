package com.kakaobank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kakaobank.evaluator.application.EvaluatorConsumer;
import com.kakaobank.evaluator.application.EvaluatorProducer;
import com.kakaobank.evaluator.application.EvaluatorService;
import com.kakaobank.evaluator.application.Rule;
import com.kakaobank.evaluator.application.rule.AccountRule;
import com.kakaobank.evaluator.application.rule.CustomerRule;
import com.kakaobank.evaluator.application.rule.MoneyRule;
import com.kakaobank.evaluator.application.rule.NewAccountRule;
import com.kakaobank.evaluator.store.EventStoreHandler;
import com.kakaobank.evaluator.suport.DateUtils;

public class Application extends Thread {

	@Override
	public void run() {

		Rule accountRule = new AccountRule(account -> DateUtils.isWithinOfTime(account.getCreateDate(), 48));
		Rule customerRule = new CustomerRule(registration -> DateUtils.calculateAge(registration.getBirthDate()) > 60);
		Rule moneyRule = new MoneyRule(balance -> balance <= 1000000 );
		Rule newAccountRule = new NewAccountRule(time -> time <= 2);

		new EvaluatorConsumer(new EvaluatorService(new EvaluatorProducer(), Arrays.asList(accountRule, customerRule, moneyRule, newAccountRule)), new EventStoreHandler()).start();
	}

	public static void main(String[] args) throws InterruptedException {
		Application application = new Application();
		Thread.sleep(1000);
		Runtime.getRuntime().addShutdownHook(application);
	}
}

