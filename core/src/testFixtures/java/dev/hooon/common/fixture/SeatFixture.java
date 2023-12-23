package dev.hooon.common.fixture;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.domain.entity.seat.SeatStatus;

/**
 * 동일한 타입을 리턴하는 메소드는 오버라이딩으로 구현
 * 불가능한 경우에만 별도의 메소드 명명
 */
public class SeatFixture {

	private SeatFixture() {
	}

	// show 에 대한 정보가 필요없는 경우에 사용
	public static Seat getSeat(LocalDate showDate, int round, LocalTime startTime) {
		Show show = new Show();
		ReflectionTestUtils.setField(show, "id", 1L);

		return Seat.of(
			show,
			SeatGrade.VIP,
			true,
			"1층",
			"A",
			10,
			100000,
			showDate,
			round,
			startTime,
			SeatStatus.AVAILABLE
		);
	}
}
