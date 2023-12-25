package dev.hooon.waitingbooking.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dev.hooon.common.exception.ValidationException;
import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.exception.WaitingBookingErrorCode;

@DisplayName("[WaitingBooking 테스트]")
class WaitingBookingTest {

	@Test
	@DisplayName("[WaitingBooking 엔티티를 생성한다]")
	void ofTest_1() {
		//given
		User user = new User();
		int seatCount = 3;
		List<Long> seatIds = List.of(1L, 2L, 3L, 4L, 5L);

		//when
		WaitingBooking result = WaitingBooking.of(user, seatCount, seatIds);

		//then
		assertAll(
			() -> assertThat(result.getSeatCount()).isEqualTo(seatCount),
			() -> assertThat(result.getStatus()).isEqualTo(WaitingStatus.WAITING),
			() -> assertThat(result.getUser()).isEqualTo(user),
			() -> {
				List<Long> actualSeatIds = result.waitingBookingSeats.stream()
					.map(WaitingBookingSeat::getSeatId)
					.toList();
				assertThat(actualSeatIds)
					.hasSameSizeAs(seatIds)
					.containsAll(seatIds);
			}
		);
	}

	@DisplayName("[엔티티 생성 시 SeatCount 가 1~3 범위에 밖의 값이면 예외를 던진다]")
	@ParameterizedTest(name = "seatCount : {0} ")
	@ValueSource(ints = {0, 4})
	void ofTest_2(int seatCount) {
		//given
		User user = new User();
		List<Long> seatIds = List.of(1L, 2L, 3L, 4L, 5L);

		//when, then
		assertThatThrownBy(() -> WaitingBooking.of(user, seatCount, seatIds))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(WaitingBookingErrorCode.INVALID_SEAT_COUNT.getMessage());
	}

	@Test
	@DisplayName("[엔티티 생성 시 SeatIds 가 비어있으면 예외를 던진다]")
	void ofTest_3() {
		//given
		User user = new User();
		int seatCount = 3;
		List<Long> seatIds = new ArrayList<>();

		//when, then
		assertThatThrownBy(() -> WaitingBooking.of(user, seatCount, seatIds))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(WaitingBookingErrorCode.EMPTY_SELECTED_SEAT.getMessage());
	}

	@Test
	@DisplayName("[엔티티 생성 시 SeatIds 개수가 seatCount 보다 적거나 10배수보다 많으면 예외를 던진다]")
	void ofTest_4() {
		//given
		User user = new User();
		int seatCount = 2;
		List<Long> overSeatIds = new ArrayList<>();
		for (long i = 0; i < 21L; i++) {
			overSeatIds.add(i);
		}
		List<Long> underSeatIds = List.of(1L);

		//when, then
		// 많을 때
		assertThatThrownBy(() -> WaitingBooking.of(user, seatCount, overSeatIds))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(WaitingBookingErrorCode.INVALID_SELECTED_SEAT_COUNT.getMessage());
		// 적을 때
		assertThatThrownBy(() -> WaitingBooking.of(user, seatCount, underSeatIds))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(WaitingBookingErrorCode.INVALID_SELECTED_SEAT_COUNT.getMessage());
	}
}