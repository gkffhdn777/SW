package com.kakaobank.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class AccountNumber {

	@JsonProperty("no")
	private final String no;

	public AccountNumber(final String no) {
		this.no = no;
	}

	public static AccountNumber create() {
		final String prefix = "3333";
		final String middle = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
		final Integer random = new Random().ints(1000000, 9999999).iterator().nextInt();

		final StringBuilder builder = new StringBuilder();
		builder.append(prefix).append("-");
		builder.append(middle).append("-");
		builder.append(random);

		return new AccountNumber(builder.toString());
	}

	public String getNo() {
		return no;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccountNumber that = (AccountNumber) o;
		return Objects.equals(no, that.no);
	}

	@Override
	public int hashCode() {
		return Objects.hash(no);
	}

	@Override
	public String toString() {
		return "AccountNumber{" +
				"no='" + no + '\'' +
				'}';
	}
}
