package dev.hooon.waitingbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.waitingbooking.application.facade.WaitingBookingFacade;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WaitingBookingApiController {

	private final WaitingBookingFacade waitingBookingFacade;

	@PostMapping("/api/waiting_bookings")
	public ResponseEntity<WaitingRegisterResponse> registerWaitingBooking(
		@RequestParam(name = "userId") Long userId, // TODO 추후에 인증정보 ArgumentResolver 가 구현되면 수정 예정
		@Valid @RequestBody WaitingRegisterRequest request
	) {
		WaitingRegisterResponse waitingRegisterResponse = waitingBookingFacade.registerWaitingBooking(userId, request);
		return ResponseEntity.ok(waitingRegisterResponse);
	}
}
