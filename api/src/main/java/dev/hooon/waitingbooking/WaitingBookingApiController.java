package dev.hooon.waitingbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.waitingbooking.application.facade.WaitingBookingFacade;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import dev.hooon.waitingbooking.dto.response.WaitingRegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Waiting Booking API")
@RestController
@RequiredArgsConstructor
public class WaitingBookingApiController {

	private final WaitingBookingFacade waitingBookingFacade;

	@NoAuth
	@PostMapping("/api/waiting_bookings")
	@Operation(summary = "예매대기 등록 API", description = "예매된 좌석에 대해서 예매대기를 등록한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<WaitingRegisterResponse> registerWaitingBooking(
		@RequestParam(name = "userId") Long userId, // TODO 추후에 인증정보 ArgumentResolver 가 구현되면 수정 예정
		@Valid @RequestBody WaitingRegisterRequest request
	) {
		WaitingRegisterResponse waitingRegisterResponse = waitingBookingFacade.registerWaitingBooking(userId, request);
		return ResponseEntity.ok(waitingRegisterResponse);
	}
}
