package com.kao.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kao.evaluator.application.EvaluatorException;
import com.kao.evaluator.application.Event;
import com.kao.evaluator.application.Rule;
import com.kao.evaluator.event.Registration;
import com.kao.evaluator.infra.CrudRepository;
import com.mongodb.client.model.Filters;

public class CustomerRule implements Rule {

	private final Predicate<Registration> predicate;

	public CustomerRule(Predicate<Registration> predicate) {
		this.predicate = predicate;
	}

	@Override
	public CompletableFuture<Boolean> verify(Event event) {
		Objects.requireNonNull(event, "Registration event cannot be null.");
		return CompletableFuture.supplyAsync(() -> {
			Registration registration =
					CrudRepository.getInstance()
							.findOne(Filters.eq("customerId._id", event.getCustomerId().getId()), Registration.class);
			return predicate.test(registration);
		}).exceptionally(ex -> {
			throw new EvaluatorException("CustomerRule verification failed.", ex);
		});
	}
}
