package com.kao;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import com.kao.domain.BankProducer;
import com.kao.infra.GeneratorProducer;
import com.kao.event.RawEvent;
import com.kao.infra.CustomerBalanceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	private static final CustomerBalanceRepository repository = new CustomerBalanceRepository(new ConcurrentHashMap<>());

	private static final BankProducer<RawEvent> bankProducer = new GeneratorProducer();

	public static void main(String[] args) {
		/*LOGGER.info("성공 시나리오 start.....");
		Scenario.success(bankProducer, repository);*/
		LOGGER.info("date : {}, 실패 시나리오 start.....", LocalDateTime.now());
		Scenario.fail1(bankProducer, repository);
		/*LOGGER.info("실패 시나리오 start.....");
		Scenario.fail2(bankProducer, repository);*/
		LOGGER.info("실패 시나리오 end.....");
	}
}
