package com.kakaobank.evaluator.application;

import java.math.BigDecimal;

import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntPredicate;
import java.util.stream.StreamSupport;

import com.kakaobank.evaluator.event.Account;
import com.kakaobank.evaluator.event.Deposit;
import com.kakaobank.evaluator.event.Registration;
import com.kakaobank.evaluator.infra.CrudRepository;
import com.kakaobank.evaluator.suport.DateUtils;

import com.mongodb.client.model.Filters;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.FutureRecordMetadata;
import org.apache.kafka.common.utils.Time;

public final class EvaluatorService {

	private static final String CUSTOMER_ID = "customerId._id";

	private final EvaluatorProducer evaluatorProducer;

	private final EvaluatorRule rule;

	public EvaluatorService(final EvaluatorProducer evaluatorProducer, final EvaluatorRule rule) {
		this.evaluatorProducer = evaluatorProducer;
		this.rule = rule;
	}

	public CompletableFuture<Future<RecordMetadata>> anomalyDetection(final Event event) {
		ExecutorService executor = Executors.newFixedThreadPool(50);

		return verifyCustomer(event, rule.getUnderYears())
				.thenComposeAsync(x -> {
					if (Boolean.TRUE.equals(x)) {
						return verifyAccount(event, rule.getAccountWithinHours());
					} else {
						return CompletableFuture.supplyAsync(() -> false);
					}
				}, executor)
				.thenComposeAsync(x -> {
					if (Boolean.TRUE.equals(x)) {
						return verifyDeposit(event, rule.getInTime(), rule.getLessThanMoney());
					} else {
						return CompletableFuture.supplyAsync(() -> false);
					}
				}, executor)
				.thenComposeAsync(x -> {
					if (Boolean.TRUE.equals(x)) {
						return anomalyDetectionNotification(event);
					} else {
						return CompletableFuture.supplyAsync(() ->
								new FutureRecordMetadata(
										null,
										0L,
										0L,
										0L,
										0,
										0,
										Time.SYSTEM));
					}
				}, executor)
				.exceptionally(ex -> {
					throw new EvaluatorException("An exception occurred during an attempt to detect anomalies.", ex);
				});
	}

	private CompletableFuture<Future<RecordMetadata>> anomalyDetectionNotification(final Event event) {
		final String message = "An unusual transaction has been detected. customer : " + event.getCustomerId() + ", dateTime : " + event.getCreateDate();
		return evaluatorProducer.send(message);
	}

	private CompletableFuture<Boolean> verifyCustomer(final Event event, int age) {
		Objects.requireNonNull(event, "Registration event cannot be null.");
		return CompletableFuture.supplyAsync(() -> {
			Registration registration =
					CrudRepository.getInstance()
							.findOne(Filters.eq(CUSTOMER_ID, event.getCustomerId().getId()), Registration.class);
			IntPredicate isAge = x -> DateUtils.calculateAge(registration.getBirthDate()) > x;
			return isAge.test(age);
		});
	}

	private CompletableFuture<Boolean> verifyAccount(final Event event, int compareHour) {
		Objects.requireNonNull(event, "Account event cannot be null.");
		return CompletableFuture.supplyAsync(() -> {
			Account account = CrudRepository.getInstance().findOne(Filters.eq(CUSTOMER_ID, event.getCustomerId().getId()), Account.class);
			IntPredicate hour = x -> DateUtils.isWithinOfTime(account.getCreateDate(), x);
			return hour.test(compareHour);
		});
	}

	private CompletableFuture<Boolean> verifyDeposit(final Event event, int inTime, long lessThanMoney) {
		Objects.requireNonNull(event, "Deposit customerId cannot be null.");
		Reaction reaction = (Reaction) event;

		CompletableFuture<Spliterator<Deposit>> findDepositList
				= CompletableFuture.supplyAsync(() -> CrudRepository.getInstance().find(Filters.eq(CUSTOMER_ID, event.getCustomerId().getId()), Deposit.class));

		CompletableFuture<Deposit> findDeposit
				= CompletableFuture.supplyAsync(() -> CrudRepository.getInstance().findOne(Filters.eq(CUSTOMER_ID, event.getCustomerId().getId()), Deposit.class));
		return findDepositList.thenCombine(findDeposit, (depositList, deposit) -> {

			BigDecimal totalMoney = StreamSupport.stream(depositList, false)
					.map(x -> x.getMoney().getWon()).reduce(BigDecimal.ZERO, BigDecimal::add);

			long time = DateUtils.getTimeDifference(deposit.getCreateDate(), event.getCreateDate());

			long balance = totalMoney.longValue() - reaction.getMoney().getWon().longValue();

			return (time <= inTime && balance <= lessThanMoney);
		});
	}
}