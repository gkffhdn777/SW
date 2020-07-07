package com.kakaobank.evaluator.application;

import java.util.concurrent.CompletableFuture;

public interface Rule {
	CompletableFuture<Boolean> verify(Event event);
}
