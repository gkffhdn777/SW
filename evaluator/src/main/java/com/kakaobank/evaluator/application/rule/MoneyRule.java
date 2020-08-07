package com.kakaobank.evaluator.application.rule;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import com.kakaobank.evaluator.application.EvaluatorException;
import com.kakaobank.evaluator.application.Event;
import com.kakaobank.evaluator.application.Reaction;
import com.kakaobank.evaluator.application.Rule;
import com.kakaobank.evaluator.event.Deposit;
import com.kakaobank.evaluator.infra.CrudRepository;
import com.mongodb.client.model.Filters;

public class MoneyRule implements Rule {

	private final Predicate<Long> predicate;

	public MoneyRule(Predicate<Long> predicate) {
		this.predicate = predicate;
	}

	@Override
	public CompletableFuture<Boolean> verify(Event event) {
		Objects.requireNonNull(event, "Money event cannot be null.");
		Reaction reaction = (Reaction) event;

		return CompletableFuture.supplyAsync(() -> {
			Spliterator<Deposit> depositSpliterator = CrudRepository.getInstance().find(Filters.eq("customerId._id", event.getCustomerId().getId()), Deposit.class);

			BigDecimal totalMoney = StreamSupport.stream(depositSpliterator, false)
					.map(x -> x.getMoney().getWon()).reduce(BigDecimal.ZERO, BigDecimal::add);

			long balance = totalMoney.longValue() - reaction.getMoney().getWon().longValue();
			return predicate.test(balance);
		}).exceptionally(ex -> {
			throw new EvaluatorException("MoneyRule verification failed.", ex);
		});
	}
}
