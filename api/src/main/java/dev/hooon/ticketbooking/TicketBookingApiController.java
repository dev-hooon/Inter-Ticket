package dev.hooon.ticketbooking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.booking.application.fascade.TicketBookingFacade;
import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TicketBookingApiController {

	private final TicketBookingFacade ticketBookingFacade;

	@PostMapping("/api/bookings")
	public ResponseEntity<TicketBookingResponse> bookingTicket(
		@RequestParam(name = "userId") Long userId, // TODO
		@Valid @RequestBody TicketBookingRequest ticketBookingRequest
	) {
		TicketBookingResponse ticketBookingResponse = ticketBookingFacade.bookingTicket(userId, ticketBookingRequest);
		return ResponseEntity.ok(ticketBookingResponse);
	}
}
