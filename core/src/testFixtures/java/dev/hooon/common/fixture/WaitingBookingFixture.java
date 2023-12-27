package dev.hooon.common.fixture;

import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;

public final class WaitingBookingFixture {

	private WaitingBookingFixture() {
	}

	public static WaitingBooking getWaitingBooking(User user) {
		return WaitingBooking.of(
			user,
			3,
			List.of(1L, 2L, 3L, 4L, 5L)
		);
	}

	public static WaitingBooking getWaitingBooking(
		Long waitingBookingId,
		int seatCount,
		List<Long> seatIds
	) {
		WaitingBooking waitingBooking = WaitingBooking.of(
			new User(),
			seatCount,
			seatIds
		);
		ReflectionTestUtils.setField(waitingBooking, "id", waitingBookingId);
		return waitingBooking;
	}
}
