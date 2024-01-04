package dev.hooon.common.fixture;

import static dev.hooon.show.domain.entity.seat.SeatStatus.*;

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

	private static final Show SHOW; // Show 정보가 필요없을때 사용하는 ID 를 가진 Show

	static {
		SHOW = new Show();
		ReflectionTestUtils.setField(SHOW, "id", 1L);
	}

	private SeatFixture() {
	}

	// show 에 대한 정보가 필요없는 경우에 사용
	public static Seat getSeat(LocalDate showDate, int round, LocalTime startTime) {
		return Seat.of(
			SHOW,
			SeatGrade.VIP,
			true,
			"1층",
			"A",
			10,
			100000,
			showDate,
			round,
			startTime,
			AVAILABLE
		);
	}

	// show 에 대한 정보가 필요없는 경우에 사용
	public static Seat getSeat() {
		return getSeat(LocalDate.now(), 1, LocalTime.now());
	}

	public static Seat getSeat(SeatStatus status) {
		return Seat.of(
			SHOW,
			SeatGrade.VIP,
			true,
			"1층",
			"A",
			10,
			100000,
			LocalDate.now(),
			1,
			LocalTime.now(),
			status
		);
	}

	public static Seat getSeatWithIsSeat(boolean isSeat) {
		return Seat.of(
			SHOW,
			SeatGrade.VIP,
			isSeat,
			"1층",
			"A",
			10,
			100000,
			LocalDate.now(),
			1,
			LocalTime.now(),
			AVAILABLE
		);
	}

	public static Seat getSeat(Long seatId) {
		Seat seat = getSeat(LocalDate.now(), 1, LocalTime.now());
		ReflectionTestUtils.setField(seat, "id", seatId);
		return seat;
	}

	public static Seat getSeat(Show show) {
		return Seat.of(
			show,
			SeatGrade.VIP,
			true,
			"1층",
			"A",
			10,
			100000,
			LocalDate.now(),
			1,
			LocalTime.now(),
			AVAILABLE
		);
	}
}
