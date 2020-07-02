package com.kakaobank.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Money {
	public static final Money ZERO = Money.wons(0);

	@JsonProperty("won")
	private final BigDecimal amount;

	Money(final BigDecimal amount) {
		if (amount.intValue() < 0) {
			throw new IllegalArgumentException("Your account does not have enough money available to cover a payment.");
		}
		this.amount = amount;
	}

	public static Money wons(final long amount) {
		return new Money(BigDecimal.valueOf(amount));
	}

	public Money plus(final Money amount) {
		if (amount == null) {
			throw new IllegalArgumentException("There is no customer money.");
		}
		return new Money(this.amount.add(amount.amount));
	}

	public Money minus(final Money amount) {
		if (amount == null) {
			throw new IllegalArgumentException("There is no customer money.");
		}
		return new Money(this.amount.subtract(amount.amount));
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Money money = (Money) o;
		return Objects.equals(amount, money.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}

	@Override
	public String toString() {
		return "Money{" +
				"amount=" + amount +
				'}';
	}
}
