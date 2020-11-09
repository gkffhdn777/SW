package com.kao.evaluator.application;

import java.time.LocalDateTime;

import com.kao.evaluator.event.CustomerId;

public interface Event {
	CustomerId getCustomerId();

	LocalDateTime getCreateDate();
}
