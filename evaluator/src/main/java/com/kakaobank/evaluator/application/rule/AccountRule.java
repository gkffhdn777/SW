package com.kakaobank.evaluator.application.rule;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.kakaobank.evaluator.application.Event;
import com.kakaobank.evaluator.application.Rule;
import com.kakaobank.evaluator.event.Account;
import com.kakaobank.evaluator.infra.CrudRepository;
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
			throw new RuntimeException("AccountRule verification failed.", ex);
		});
	}
}
