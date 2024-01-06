package dev.hooon.show.domain.entity.seat;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.hooon.common.fixture.SeatFixture;

@DisplayName("[Seat 도메인 단위 테스트]")
class SeatTest {

	@DisplayName("해당 좌석의 isSeat값이 false이면 해당 좌석은 예매 가능하지 않다.")
	@Test
	void isBookingAvailableTest1() {
		// given
		Seat seat = SeatFixture.getSeatWithIsSeat(false);

		// when, then
		assertFalse(seat.isBookingAvailable());
	}

	@DisplayName("해당 좌석의 seatStatus 값이 AVAILABLE이 아니면 해당 좌석은 예매 가능하지 않다.")
	@Test
	void isBookingAvailableTest2() {
		// given
		Seat seat1 = SeatFixture.getSeat(SeatStatus.BOOKED);
		Seat seat2 = SeatFixture.getSeat(SeatStatus.WAITING);
		Seat seat3 = SeatFixture.getSeat(SeatStatus.CANCELED);

		// when, then
		assertAll(
			() -> assertFalse(seat1.isBookingAvailable()),
			() -> assertFalse(seat2.isBookingAvailable()),
			() -> assertFalse(seat3.isBookingAvailable())
		);
	}

	@DisplayName("해당 좌석이 예매가 되면, 해당 좌석의 SeatStatus는 'BOOKED'으로 변경된다")
	@Test
	void markSeatStatusAsBookedTest() {
		// given
		Seat seat = SeatFixture.getSeat();

		// when
		seat.markSeatStatusAsBooked();

		// then
		assertThat(seat.getSeatStatus()).isEqualTo(SeatStatus.BOOKED);
	}
}
