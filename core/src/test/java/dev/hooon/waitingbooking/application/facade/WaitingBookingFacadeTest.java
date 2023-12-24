package dev.hooon.waitingbooking.application.facade;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

	@Test
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
}