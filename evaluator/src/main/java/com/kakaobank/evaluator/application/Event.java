package com.kakaobank.evaluator.application;

import java.time.LocalDateTime;

import com.kakaobank.evaluator.event.CustomerId;

public interface Event {
	CustomerId getCustomerId();

	LocalDateTime getCreateDate();
}
