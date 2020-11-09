package com.kao.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kao.evaluator.application.EvaluatorException;
import com.kao.evaluator.application.Event;
import com.kao.evaluator.application.Rule;
import com.kao.evaluator.event.Deposit;
import com.kao.evaluator.infra.CrudRepository;
import com.kao.evaluator.suport.DateUtils;
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
