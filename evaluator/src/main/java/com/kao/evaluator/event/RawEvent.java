package com.kao.evaluator.event;

import java.time.LocalDateTime;

public final class RawEvent {

	private String id;

	private BankActionType bankActionType;

	private String payload;

	private LocalDateTime dateTime;

	public RawEvent() {
	}

	public RawEvent(String id, BankActionType bankActionType, String payload, LocalDateTime dateTime) {
		this.id = id;
		this.bankActionType = bankActionType;
		this.payload = payload;
		this.dateTime = dateTime;
	}

	public String getId() {
		return id;
	}

	public BankActionType getBankActionType() {
		return bankActionType;
	}

	public String getPayload() {
		return payload;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
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
