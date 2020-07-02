package com.kakaobank.infra;

public class CustomerBalanceException extends RuntimeException {
	public CustomerBalanceException(String message) {
		super(message);
	}
}
