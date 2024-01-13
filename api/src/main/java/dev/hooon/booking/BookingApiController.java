package dev.hooon.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.jwt.JwtAuthorization;
import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.dto.response.BookingListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookingApiController {

	private final BookingService bookingService;

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
}
