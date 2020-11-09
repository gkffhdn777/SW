package com.kao.evaluator.suport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

	private DateUtils() {
	}

	public static int calculateAge(final LocalDate birthDate) {
		if (birthDate != null) {
			return Period.between(birthDate, LocalDate.now()).getYears();
		} else {
			return 0;
		}
	}

	public static Boolean isWithinOfTime(final LocalDateTime accountDate, final int hour) {
		if (accountDate == null) {
			throw new NullPointerException("AccountDate cannot be null");
		}
		long time = getTimeDifference(accountDate, LocalDateTime.now());
		return time <= hour;
	}

	public static long getTimeDifference(final LocalDateTime startDate, final LocalDateTime endDate) {
		if (startDate == null || endDate == null) {
			throw new NullPointerException("LocalDateTime cannot be null");
		}
		long hour = ChronoUnit.HOURS.between(startDate, endDate);
		if (hour <= 0) {
			return 0L;
		}
		return hour;
	}
}
