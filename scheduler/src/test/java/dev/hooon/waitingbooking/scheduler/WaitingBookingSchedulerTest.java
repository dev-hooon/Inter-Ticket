package dev.hooon.waitingbooking.scheduler;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.support.TestContainerSupport;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.entity.UserRole;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;

@DisplayName("[WaitingBookingScheduler 테스트]")
@SpringBootTest
class WaitingBookingSchedulerTest extends TestContainerSupport {

	@Autowired
	private WaitingBookingScheduler waitingBookingScheduler;
	@Autowired
	private WaitingBookingRepository waitingBookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SeatRepository seatRepository;

	private void assertSeatStatus(Long id, SeatStatus expected) {
		assertThat(seatRepository.findById(id)).isPresent()
			.get()
			.extracting("seatStatus")
			.isEqualTo(expected);
	}

	private void assertWaitingBookingStatus(Long id, WaitingStatus expected) {
		assertThat(waitingBookingRepository.findById(id)).isPresent()
			.get()
			.extracting("status")
			.isEqualTo(expected);
	}

	@Test
	@DisplayName("[현재 대기중인 예약대기를 처리한다]")
	void scheduleWaitingBookingProcess() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(SeatStatus.CANCELED),
			SeatFixture.getSeat(SeatStatus.CANCELED),
			SeatFixture.getSeat(SeatStatus.CANCELED)
		);
		seatRepository.saveAll(seats);

		User user = new User("hello123@naver.com", "name", UserRole.BUYER);
		userRepository.save(user);

		List<WaitingBooking> waitingBookings = List.of(
			WaitingBooking.of(
				user,
				1,
				List.of(1000L, seats.get(0).getId(), 2000L)
			),
			WaitingBooking.of(
				user,
				1,
				List.of(seats.get(1).getId(), 2000L, 3000L)
			),
			WaitingBooking.of(
				user,
				1,
				List.of(1000L, 2000L, 3000L)
			)
		);
		waitingBookings.forEach(waitingBooking -> waitingBookingRepository.save(waitingBooking));

		//when
		waitingBookingScheduler.scheduleWaitingBookingProcess();

		//then
		assertSeatStatus(seats.get(0).getId(), SeatStatus.WAITING);
		assertSeatStatus(seats.get(1).getId(), SeatStatus.WAITING);
		assertSeatStatus(seats.get(2).getId(), SeatStatus.AVAILABLE);

		assertWaitingBookingStatus(waitingBookings.get(0).getId(), WaitingStatus.ACTIVATION);
		assertWaitingBookingStatus(waitingBookings.get(1).getId(), WaitingStatus.ACTIVATION);
		assertWaitingBookingStatus(waitingBookings.get(2).getId(), WaitingStatus.WAITING);
	}
}