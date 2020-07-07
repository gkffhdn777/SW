package com.kakaobank.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kakaobank.evaluator.application.Event;
import com.kakaobank.evaluator.application.Rule;
import com.kakaobank.evaluator.event.Registration;
import com.kakaobank.evaluator.infra.CrudRepository;
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
			throw new RuntimeException("CustomerRule verification failed.", ex);
		});
	}
}
