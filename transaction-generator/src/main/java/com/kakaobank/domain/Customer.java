package com.kakaobank.domain;

import java.time.LocalDate;

public final class Customer {
	private final String name;

	private final LocalDate birthDate;

	public Customer(final String name, final LocalDate birthDate) {
		if (name == null && birthDate == null) {
			throw new IllegalArgumentException("고객 정보가 잘못되었습니다.");
		}
		this.name = name;
		this.birthDate = birthDate;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"name='" + name + '\'' +
				", birthDate=" + birthDate +
				'}';
	}
}
