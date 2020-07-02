package com.kakaobank.event;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RawEvent {

	@JsonProperty("id")
	private final String id;

	@JsonProperty("bankActionType")
	private final BankActionType bankActionType;

	@JsonProperty("payload")
	private final String payload;

	@JsonProperty("dateTime")
	private final LocalDateTime dateTime;

	public RawEvent() {
		this(BankActionType.NONE, null);
	}

	public <T> RawEvent(final BankActionType bankActionType, final T payload) {
		this.id = UUID.randomUUID().toString();
		this.bankActionType = bankActionType;
		this.payload = JsonMapper.writeValueAsString(payload);
		this.dateTime = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "RawEvent{" +
				"id='" + id + '\'' +
				", bankActionType=" + bankActionType +
				", payload='" + payload + '\'' +
				", dateTime=" + dateTime +
				'}';
	}
}
