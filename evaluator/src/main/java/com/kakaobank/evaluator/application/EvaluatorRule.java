package com.kakaobank.evaluator.application;

public final class EvaluatorRule {

	private final int underYears;

	private final int accountWithinHours;

	private final int inTime;

	private final long lessThanMoney;

	public EvaluatorRule(final int underYears, final int accountWithinHours, final int inTime, final long lessThanMoney) {
		this.underYears = underYears;
		this.accountWithinHours = accountWithinHours;
		this.lessThanMoney = lessThanMoney;
		this.inTime = inTime;
	}

	public int getUnderYears() {
		return underYears;
	}

	public int getAccountWithinHours() {
		return accountWithinHours;
	}

	public long getLessThanMoney() {
		return lessThanMoney;
	}

	public int getInTime() {
		return inTime;
	}

	@Override
	public String toString() {
		return "EvaluatorRule{" +
				"underYears=" + underYears +
				", accountWithinHours=" + accountWithinHours +
				", lessThanMoney=" + lessThanMoney +
				", inTime=" + inTime +
				'}';
	}
}
