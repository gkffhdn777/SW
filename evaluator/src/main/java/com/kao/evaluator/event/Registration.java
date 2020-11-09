package com.kao.evaluator.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kao.evaluator.application.Event;

public final class Registration implements Event {
	private CustomerId customerId;

	private String name;

	private LocalDate birthDate;

	private LocalDateTime createDate;

	@Override
	public CustomerId getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	@Override
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Registration{" +
				"customerId=" + customerId +
				", name='" + name + '\'' +
				", birthDate=" + birthDate +
				", createDate=" + createDate +
				'}';
	}
}
