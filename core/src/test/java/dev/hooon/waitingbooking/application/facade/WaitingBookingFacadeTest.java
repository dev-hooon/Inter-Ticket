package dev.hooon.waitingbooking.application.facade;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.common.fixture.WaitingBookingFixture;
import dev.hooon.show.application.SeatService;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.application.WaitingBookingService;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;

@DisplayName("[WaitingBookingFacade 테스트]")
@ExtendWith(MockitoExtension.class)
class WaitingBookingFacadeTest {

	@InjectMocks
	private WaitingBookingFacade waitingBookingFacade;
	@Mock
	private WaitingBookingService waitingBookingService;
	@Mock
	private UserService userService;
	@Mock
	private SeatService seatService;

	@Test
	@DisplayName("[사용자와 예약대기 정보를 통해 예약대기를 등록한다]")
	void registerWaitingBookingTest() {
		//given
		User user = new User();
		WaitingRegisterRequest request = new WaitingRegisterRequest(3, List.of(1L, 2L, 3L));
		WaitingBooking waitingBooking = WaitingBooking.of(user, request.seatCount(), request.seatIds());
		// 테스트하는 로직에 waitingBooking 의 ID 가 필요하기 때문에 리플렉션으로 주입
		ReflectionTestUtils.setField(waitingBooking, "id", 1L);

		given(userService.getUserById(1L)).willReturn(user);
		given(waitingBookingService.createWaitingBooking(user, request))
			.willReturn(waitingBooking);

		//when
		WaitingRegisterResponse result = waitingBookingFacade.registerWaitingBooking(1L, request);

		//then
		assertThat(result.waitingBookingId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("[현재 대기중인 예약대기를 처리한다]")
	void processWaitingBookingTest() {
		//given
		Set<Long> canceledSeatIds = new HashSet<>();
		// 1L~10L 값세팅
		for (long i = 1L; i <= 10L; i++) {
			canceledSeatIds.add(i);
		}
		given(seatService.getCanceledSeatIds())
			.willReturn(canceledSeatIds);

		List<WaitingBooking> waitingBookings = List.of(
			WaitingBookingFixture.getWaitingBooking(1L, 3, List.of(1L, 2L, 3L, 4L)),
			WaitingBookingFixture.getWaitingBooking(2L, 1, List.of(1L, 2L, 4L)),
			WaitingBookingFixture.getWaitingBooking(3L, 2, List.of(1L, 2L, 3L, 4L, 5L))
		);
		given(waitingBookingService.getWaitingBookingsByStatusIsWaiting())
			.willReturn(waitingBookings);

		//when
		waitingBookingFacade.processWaitingBooking();

		//then
		verify(seatService, times(2)).updateSeatToWaiting(anyCollection());
		verify(waitingBookingService, times(1)).activateWaitingBooking(eq(waitingBookings.get(0).getId()), anyList());
		verify(waitingBookingService, times(1)).activateWaitingBooking(eq(waitingBookings.get(1).getId()), anyList());
		verify(seatService, times(1)).updateSeatToAvailable(anyCollection());
	}

	@Test
	@DisplayName("[만료된 활성화 상태인 예약대기를 처리한다]")
	void processExpiredWaitingBooking_test() {
		//given
		LocalDateTime beforeNow = LocalDateTime.now().minusSeconds(10);
		LocalDateTime afterNow = LocalDateTime.now().plusSeconds(10);
		List<WaitingBooking> waitingBookings = List.of(
			WaitingBookingFixture.getActiveWaitingBooking(1L, beforeNow, 2, List.of(1L, 2L)),
			WaitingBookingFixture.getActiveWaitingBooking(2L, beforeNow, 2, List.of(3L, 4L)),
			WaitingBookingFixture.getActiveWaitingBooking(3L, afterNow, 2, List.of(5L, 6L))
		);

		given(waitingBookingService.getWaitingBookingsByStatusIsActivation())
			.willReturn(waitingBookings);

		//when
		waitingBookingFacade.processExpiredWaitingBooking();

		//then
		verify(waitingBookingService, times(1)).expireActiveWaitingBooking(anyList());
		verify(seatService, times(1)).updateSeatToAvailable(anyList());
	}
}