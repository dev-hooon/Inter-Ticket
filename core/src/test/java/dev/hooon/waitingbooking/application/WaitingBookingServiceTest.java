package dev.hooon.waitingbooking.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
import dev.hooon.waitingbooking.domain.entity.waitingbookingseat.SelectedSeat;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;

@DisplayName("[WaitingBookingService 테스트]")
@ExtendWith(MockitoExtension.class)
class WaitingBookingServiceTest {

	@InjectMocks
	private WaitingBookingService waitingBookingService;
	@Mock
	private WaitingBookingRepository waitingBookingRepository;

	@Test
	@DisplayName("[WaitingBooking 을 생성하고 응답한다]")
	void createWaitingBookingTest() {
		//given
		int seatCount = 2;
		List<Long> seatIds = List.of(1L, 2L, 3L);
		WaitingRegisterRequest request = new WaitingRegisterRequest(seatCount, seatIds);
		User user = new User();

		//when
		WaitingBooking result = waitingBookingService.createWaitingBooking(user, request);

		//then
		assertAll(
			() -> assertThat(result.getSeatCount()).isEqualTo(seatCount),
			() -> assertThat(result.getStatus()).isEqualTo(WaitingStatus.WAITING),
			() -> assertThat(result.getUser()).isEqualTo(user),
			() -> {
				List<Long> actualSeatIds = result.getSelectedSeats().stream()
					.map(SelectedSeat::getSeatId)
					.toList();
				assertThat(actualSeatIds)
					.hasSameSizeAs(seatIds)
					.containsAll(seatIds);
			}
		);
	}
}