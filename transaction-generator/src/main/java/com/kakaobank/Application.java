package com.kakaobank;

import java.util.concurrent.ConcurrentHashMap;

import com.kakaobank.domain.BankProducer;
import com.kakaobank.infra.GeneratorProducer;
import com.kakaobank.event.RawEvent;
import com.kakaobank.infra.CustomerBalanceRepository;

public class Application {

	private static final CustomerBalanceRepository repository = new CustomerBalanceRepository(new ConcurrentHashMap<>());

	private static final BankProducer<RawEvent> bankProducer = new GeneratorProducer();

	public static void main(String[] args) {

		Scenario.success(bankProducer, repository);

		Scenario.fail(bankProducer, repository);

	}
}
