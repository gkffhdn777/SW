package com.kao.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kao.evaluator.application.EvaluatorException;
import com.kao.evaluator.application.Event;
import com.kao.evaluator.application.Rule;
import com.kao.evaluator.event.Account;
import com.kao.evaluator.infra.CrudRepository;
import com.mongodb.client.model.Filters;

public class AccountRule implements Rule {

	private final Predicate<Account> predicate;

	public AccountRule(Predicate<Account> predicate) {
		this.predicate = predicate;
	}

	@Override
	public CompletableFuture<Boolean> verify(Event event) {
		Objects.requireNonNull(event, "Account event cannot be null.");
		return CompletableFuture.supplyAsync(() -> {
			Account account = CrudRepository.getInstance().findOne(Filters.eq("customerId._id", event.getCustomerId().getId()), Account.class);
			return predicate.test(account);
		}).exceptionally(ex -> {
			throw new EvaluatorException("AccountRule verification failed.", ex);
		});
	}
}
