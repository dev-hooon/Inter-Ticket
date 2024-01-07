package dev.hooon.waitingbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.NoAuth;
import dev.hooon.auth.JwtAuthorization;
import dev.hooon.waitingbooking.application.facade.WaitingBookingFacade;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WaitingBookingApiController {

	private final WaitingBookingFacade waitingBookingFacade;

	@NoAuth
	@PostMapping("/api/waiting_bookings")
	public ResponseEntity<WaitingRegisterResponse> registerWaitingBooking(
		@Valid @RequestBody WaitingRegisterRequest request,
		@JwtAuthorization Long userId
	) {
		WaitingRegisterResponse waitingRegisterResponse = waitingBookingFacade.registerWaitingBooking(userId, request);
		return ResponseEntity.ok(waitingRegisterResponse);
	}
}
