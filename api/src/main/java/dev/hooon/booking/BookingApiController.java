package dev.hooon.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.auth.jwt.JwtAuthorization;
import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.application.fascade.TicketBookingFacade;
import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.booking.dto.response.BookingCancelResponse;
import dev.hooon.booking.dto.response.BookingListResponse;
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
public class BookingApiController {

	private final BookingService bookingService;
	private final TicketBookingFacade ticketBookingFacade;

	@GetMapping("/api/users/booking")
	@Operation(summary = "예매 조회 API", description = "예매를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<BookingListResponse> getBookings(
		@Parameter(hidden = true) @JwtAuthorization Long userId,
		@RequestParam(name = "duration") int duration,
		@PageableDefault(size = 10) Pageable pageable
	) {
		BookingListResponse bookingListResponse = bookingService.getBookings(
			userId,
			duration,
			pageable
		);
		return ResponseEntity.ok(bookingListResponse);
	}

	@PostMapping("/api/bookings/cancel/{booking_id}")
	@Operation(summary = "예매 취소 API", description = "예매를 취소한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<BookingCancelResponse> cancelBooking(
		@Parameter(hidden = true) @JwtAuthorization Long userId,
		@PathVariable("booking_id") Long bookingId
	) {
		BookingCancelResponse bookingCancelResponse = bookingService.cancelBooking(
			userId,
			bookingId
		);
		return ResponseEntity.ok(bookingCancelResponse);
	}

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
