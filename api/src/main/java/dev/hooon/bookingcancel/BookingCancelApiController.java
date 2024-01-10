package dev.hooon.bookingcancel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.dto.response.BookingCancelResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Booking API")
@RestController
@RequiredArgsConstructor
public class BookingCancelApiController {

	private final BookingService bookingService;

	@NoAuth
	@PostMapping("/api/bookings/cancel/{booking_id}")
	@Operation(summary = "예매 취소 API", description = "예매를 취소한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<BookingCancelResponse> cancelBooking(
		@RequestParam(name = "userId") Long userId, // TODO
		@PathVariable("booking_id") Long bookingId
	) {
		BookingCancelResponse bookingCancelResponse = bookingService.cancelBooking(
			userId,
			bookingId
		);
		return ResponseEntity.ok(bookingCancelResponse);
	}
}
