package dev.hooon.show.application;

import static dev.hooon.show.exception.ShowErrorCode.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.function.Supplier;

import dev.hooon.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PeriodType {

	// 오늘 자정을 기준
	DAY(() -> LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIDNIGHT)),

	// 한주의 시작을 목요일로 설정(변경한다면 해당 코드를 변경하면 됨)
	WEEK(() -> {
		LocalDate startDate = LocalDateTime.now()
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))
			.toLocalDate();
		return LocalDateTime.of(startDate, LocalTime.MIDNIGHT);
	}),

	// 한달의 시작은 매월 1일
	MONTH(() -> {
		LocalDate startDate = LocalDateTime.now()
			.with(TemporalAdjusters.firstDayOfMonth())
			.toLocalDate();
		return LocalDateTime.of(startDate, LocalTime.MIDNIGHT);
	});

	private final Supplier<LocalDateTime> startAtSupplier;

	public static PeriodType of(String target) {
		return Arrays.stream(values())
			.filter(periodType -> periodType.name().equalsIgnoreCase(target))
			.findAny()
			.orElseThrow(() -> new NotFoundException(SHOW_PERIOD_TYPE_NOT_FOUND));
	}

	public LocalDateTime getStartAt() {
		return startAtSupplier.get();
	}
}
