package com.kakaobank.evaluator.application;

public class EvaluatorException extends RuntimeException {
	public EvaluatorException(String message) {
		super(message);
	}

	public EvaluatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
