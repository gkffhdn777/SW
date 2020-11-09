package com.kao.evaluator.event;

import java.util.Objects;

public final class CustomerId {

	private String id;

	public CustomerId() {
	}

	public CustomerId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
