package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Show API")
@RestController
@RequiredArgsConstructor
public class ShowApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows/{show_id}/available")
	@Operation(summary = "공연 예매가능 날짜, 회차 조회 API", description = "공연의 예매 가능한 날짜와 회차 정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<AbleBookingDateRoundResponse> getAbleBookingDateRoundInfo(
		@PathVariable("show_id") Long showId
	) {
		AbleBookingDateRoundResponse ableBookingDateRoundResponse = showService.getAbleBookingDateRoundInfo(showId);
		return ResponseEntity.ok(ableBookingDateRoundResponse);
	}
}
