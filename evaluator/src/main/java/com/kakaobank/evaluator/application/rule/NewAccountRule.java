package com.kakaobank.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kakaobank.evaluator.application.EvaluatorException;
import com.kakaobank.evaluator.application.Event;
import com.kakaobank.evaluator.application.Rule;
import com.kakaobank.evaluator.event.Deposit;
import com.kakaobank.evaluator.infra.CrudRepository;
import com.kakaobank.evaluator.suport.DateUtils;
import com.mongodb.client.model.Filters;

public class NewAccountRule implements Rule {

	private final Predicate<Long> predicate;

	public NewAccountRule(Predicate<Long> predicate) {
		this.predicate = predicate;
	}

	@Override
	public CompletableFuture<Boolean> verify(Event event) {
		Objects.requireNonNull(event, "Deposit customerId cannot be null.");
		return CompletableFuture.supplyAsync(() -> {
			Deposit deposit	= CrudRepository.getInstance().findOne(Filters.eq("customerId._id", event.getCustomerId().getId()), Deposit.class);

			long time = DateUtils.getTimeDifference(deposit.getCreateDate(), event.getCreateDate());
			return predicate.test(time);
		}).exceptionally(ex -> {
			throw new EvaluatorException("NewAccountRule verification failed.", ex);
		});
	}
}
