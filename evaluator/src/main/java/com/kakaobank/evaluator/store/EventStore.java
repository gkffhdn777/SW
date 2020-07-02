package com.kakaobank.evaluator.store;

import java.util.Objects;

import com.kakaobank.evaluator.event.Account;
import com.kakaobank.evaluator.event.Deposit;
import com.kakaobank.evaluator.event.Registration;
import com.kakaobank.evaluator.event.Transfer;
import com.kakaobank.evaluator.event.Withdrawal;
import com.kakaobank.evaluator.infra.CrudRepository;

public final class EventStore {

	private EventStore() {
	}

	private Boolean execute(final Account account) {
		Objects.requireNonNull(account, "Account cannot be null.");
		return CrudRepository.getInstance().save(account, Account.class);
	}

	private Boolean execute(final Registration registration) {
		Objects.requireNonNull(registration, "Registration cannot be null.");
		return CrudRepository.getInstance().save(registration, Registration.class);
	}

	private Boolean execute(final Deposit deposit) {
		Objects.requireNonNull(deposit, "Deposit cannot be null.");
		return CrudRepository.getInstance().save(deposit, Deposit.class);
	}

	private Boolean execute(final Transfer transfer) {
		Objects.requireNonNull(transfer, "Transfer cannot be null.");
		return CrudRepository.getInstance().save(transfer, Transfer.class);
	}

	private Boolean execute(final Withdrawal withdrawal) {
		Objects.requireNonNull(withdrawal, "Withdrawal cannot be null.");
		return CrudRepository.getInstance().save(withdrawal, Withdrawal.class);
	}
}
