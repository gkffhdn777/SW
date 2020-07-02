package com.kakaobank.domain;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CustomerId {
	@JsonProperty("id")
	private final String id;

	public CustomerId(final String id) {
		this.id = id;
	}

	public static CustomerId createUUID() {
		return new CustomerId(UUID.randomUUID().toString());
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomerId that = (CustomerId) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "CustomerId{" +
				"id=" + id +
				'}';
	}
}
