package dev.hooon.show.application;

import static dev.hooon.show.exception.ShowErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.hooon.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisplayName("[PeriodType 테스트]")
class PeriodTypeTest {

	@Test
	@DisplayName("[입력된 값에 맞는 PeriodType 을 응답한다]")
	void of_test_1() {
		//given
		String periodType = "day";

		//when
		PeriodType result = PeriodType.of(periodType);

		//then
		assertThat(result).isEqualTo(PeriodType.DAY);
	}

	@Test
	@DisplayName("[입력된 값에 맞는 PeriodType 이 존재하지 않아 실패한다]")
	void of_test_2() {
		//given
		String periodType = "days";

		//when, then
		assertThatThrownBy(() -> PeriodType.of(periodType))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(SHOW_PERIOD_TYPE_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("[일간, 주간, 월간 집계기간의 시작날짜를 구한다]")
	void getStartAt_test() {
		//given
		PeriodType day = PeriodType.DAY;
		PeriodType week = PeriodType.WEEK;
		PeriodType month = PeriodType.MONTH;

		LocalDateTime baseTime = LocalDateTime.of(2023, 11, 16, 12, 12);

		//when
		LocalDateTime dayStartAt = day.getStartAt(baseTime);
		LocalDateTime weekStartAt = week.getStartAt(baseTime);
		LocalDateTime monthStartAt = month.getStartAt(baseTime);

		//then
		assertAll(
			() -> assertThat(dayStartAt.toLocalDate()).isEqualTo(baseTime.toLocalDate()),
			() -> assertThat(weekStartAt).isBefore(baseTime),
			() -> assertThat(monthStartAt).isBefore(baseTime)
		);

		log.info("dayStartAt : {}, weekStartAt : {}, monthStartAt : {}", dayStartAt, weekStartAt, monthStartAt);
	}
}