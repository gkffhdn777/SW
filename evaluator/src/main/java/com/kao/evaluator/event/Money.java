package com.kao.evaluator.event;

import java.math.BigDecimal;

public final class Money {

	private BigDecimal won;

	public BigDecimal getWon() {
		return won;
	}

	public void setWon(BigDecimal won) {
		this.won = won;
	}

	@Override
	public String toString() {
		return "Money{" +
				"won=" + won +
				'}';
	}
}
