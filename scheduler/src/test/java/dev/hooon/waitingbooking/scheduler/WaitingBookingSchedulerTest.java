package dev.hooon.waitingbooking.scheduler;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.fixture.WaitingBookingFixture;
import dev.hooon.common.support.IntegrationTestSupport;
import dev.hooon.mail.event.WaitingBookingMailEventListener;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.event.WaitingBookingActiveEvent;
import jakarta.mail.MessagingException;

@DisplayName("[WaitingBookingScheduler 테스트]")
class WaitingBookingSchedulerTest extends IntegrationTestSupport {

	@Autowired
	private WaitingBookingScheduler waitingBookingScheduler;
	@Autowired
	private WaitingBookingRepository waitingBookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SeatRepository seatRepository;
	@Autowired
	private TestEntityManager entityManager;
	@MockBean
	private WaitingBookingMailEventListener eventListener;

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
	void scheduleWaitingBookingProcess() throws MessagingException, UnsupportedEncodingException {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(SeatStatus.CANCELED),
			SeatFixture.getSeat(SeatStatus.CANCELED),
			SeatFixture.getSeat(SeatStatus.CANCELED)
		);
		seatRepository.saveAll(seats);

		User user = User.ofBuyer("hello123@naver.com", "name", "password");
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
		entityManager.flush();
		entityManager.clear();

		//then
		assertSeatStatus(seats.get(0).getId(), SeatStatus.WAITING);
		assertSeatStatus(seats.get(1).getId(), SeatStatus.WAITING);
		assertSeatStatus(seats.get(2).getId(), SeatStatus.AVAILABLE);

		assertWaitingBookingStatus(waitingBookings.get(0).getId(), WaitingStatus.ACTIVATION);
		assertWaitingBookingStatus(waitingBookings.get(1).getId(), WaitingStatus.ACTIVATION);
		assertWaitingBookingStatus(waitingBookings.get(2).getId(), WaitingStatus.WAITING);

		verify(eventListener, times(2)).sendWaitingBookingMail(any(WaitingBookingActiveEvent.class));
	}

	@Test
	@DisplayName("[만료된 활성화 상태인 예약대기를 처리한다]")
	void scheduleExpiredWaitingBookingProcess_test() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(SeatStatus.WAITING),
			SeatFixture.getSeat(SeatStatus.WAITING),
			SeatFixture.getSeat(SeatStatus.WAITING)
		);
		seatRepository.saveAll(seats);

		User user = User.ofBuyer("hello123@naver.com", "name", "password");
		userRepository.save(user);

		LocalDateTime beforeNow = LocalDateTime.now().minusMinutes(10);
		LocalDateTime afterNow = LocalDateTime.now().plusMinutes(10);

		List<WaitingBooking> waitingBookings = List.of(
			WaitingBookingFixture.getActiveWaitingBooking(
				user, beforeNow, 1, List.of(seats.get(0).getId())
			),
			WaitingBookingFixture.getActiveWaitingBooking(
				user, beforeNow, 1, List.of(seats.get(1).getId())
			),
			WaitingBookingFixture.getActiveWaitingBooking(
				user, afterNow, 1, List.of(seats.get(2).getId())
			)
		);

		waitingBookings.forEach(waitingBooking -> waitingBookingRepository.save(waitingBooking));

		//when
		waitingBookingScheduler.scheduleExpiredWaitingBookingProcess();
		entityManager.flush();
		entityManager.clear();

		//then
		assertSeatStatus(seats.get(0).getId(), SeatStatus.AVAILABLE);
		assertSeatStatus(seats.get(1).getId(), SeatStatus.AVAILABLE);
		assertSeatStatus(seats.get(2).getId(), SeatStatus.WAITING);

		assertWaitingBookingStatus(waitingBookings.get(0).getId(), WaitingStatus.EXPIRED);
		assertWaitingBookingStatus(waitingBookings.get(1).getId(), WaitingStatus.EXPIRED);
		assertWaitingBookingStatus(waitingBookings.get(2).getId(), WaitingStatus.ACTIVATION);
	}
}
