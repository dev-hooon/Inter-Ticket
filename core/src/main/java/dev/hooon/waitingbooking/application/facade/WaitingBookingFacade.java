package dev.hooon.waitingbooking.application.facade;

import org.springframework.stereotype.Component;

import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.application.WaitingBookingService;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingBookingFacade {

	private final WaitingBookingService waitingBookingService;
	private final UserService userService;

	public WaitingRegisterResponse registerWaitingBooking(Long userId, WaitingRegisterRequest request) {
		User user = userService.getUserById(userId);
		WaitingBooking waitingBooking = waitingBookingService.createWaitingBooking(user, request);

		return new WaitingRegisterResponse(waitingBooking.getId());
	}
}
