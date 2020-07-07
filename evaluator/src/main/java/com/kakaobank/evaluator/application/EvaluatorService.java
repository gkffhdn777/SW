package com.kakaobank.evaluator.application;

import java.util.List;
import java.util.stream.Stream;

public final class EvaluatorService {

	private final EvaluatorProducer evaluatorProducer;

	private final List<Rule> rules;

	public EvaluatorService(final EvaluatorProducer evaluatorProducer, final List<Rule> rules) {
		this.evaluatorProducer = evaluatorProducer;
		this.rules = rules;
	}

	public Boolean anomalyDetection(final Event event) {
		boolean match = rules.stream()
				.flatMap(x -> Stream.of(x.verify(event)))
				.flatMap(x -> Stream.of(x.join()))
				.allMatch(x -> x);
		if (!match) {
			anomalyDetectionNotification(event);
		}
		return match;
	}

	private void anomalyDetectionNotification(final Event event) {
		final String message = "An unusual transaction has been detected. customer : " + event.getCustomerId() + ", dateTime : " + event.getCreateDate();
		evaluatorProducer.send(message);
	}
}