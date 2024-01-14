package dev.hooon.ticketbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.auth.jwt.JwtAuthorization;
import dev.hooon.booking.application.fascade.TicketBookingFacade;
import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Booking API")
@RestController
@RequiredArgsConstructor
public class TicketBookingApiController {

	private final TicketBookingFacade ticketBookingFacade;

	@NoAuth
	@PostMapping("/api/bookings")
	@Operation(summary = "티켓 예매 API", description = "티켓을 예매한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<TicketBookingResponse> bookingTicket(
		@Parameter(hidden = true) @JwtAuthorization Long userId,
		@Valid @RequestBody TicketBookingRequest ticketBookingRequest
	) {
		TicketBookingResponse ticketBookingResponse = ticketBookingFacade.bookingTicket(
			userId,
			ticketBookingRequest.seatIds()
		);
		return ResponseEntity.ok(ticketBookingResponse);
	}
}
