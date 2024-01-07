package dev.hooon.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.dto.response.BookingListResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookingApiController {

	private final BookingService bookingService;

	@GetMapping("/api/users/booking")
	public ResponseEntity<BookingListResponse> getBookings(
		@RequestParam(name = "userId") Long userId, // TODO
		@RequestParam(name = "duration") int days,
		@PageableDefault(size = 10) Pageable pageable
	) {
		BookingListResponse bookingListResponse = bookingService.getBookings(
			userId,
			days,
			pageable
		);
		return ResponseEntity.ok(bookingListResponse);
	}
}
