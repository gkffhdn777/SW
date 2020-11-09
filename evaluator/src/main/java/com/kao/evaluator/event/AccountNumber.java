package com.kao.evaluator.event;

public final class AccountNumber {

	private String no;

	public AccountNumber() {
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "AccountNumber{" +
				"no='" + no + '\'' +
				'}';
	}
}
