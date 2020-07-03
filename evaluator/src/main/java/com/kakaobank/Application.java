package com.kakaobank;

import com.kakaobank.evaluator.application.EvaluatorConsumer;
import com.kakaobank.evaluator.application.EvaluatorProducer;
import com.kakaobank.evaluator.application.EvaluatorRule;
import com.kakaobank.evaluator.application.EvaluatorService;
import com.kakaobank.evaluator.store.EventStoreHandler;

public class Application extends Thread {

	@Override
	public void run() {
		new EvaluatorConsumer(new EvaluatorService(new EvaluatorProducer(),
				new EvaluatorRule(60, 48, 2, 1)), new EventStoreHandler()).start();
	}

	public static void main(String[] args) throws InterruptedException {
		Application application = new Application();
		Thread.sleep(1000);
		Runtime.getRuntime().addShutdownHook(application);
	}
}

