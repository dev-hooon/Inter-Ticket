package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowApiController {

	private final ShowService showService;

	@GetMapping("/api/shows/{show_id}/available")
	public ResponseEntity<AbleBookingDateRoundResponse> getAbleBookingDateRoundInfo(
		@PathVariable("show_id") Long showId
	) {
		AbleBookingDateRoundResponse ableBookingDateRoundResponse = showService.getAbleBookingDateRoundInfo(showId);
		return ResponseEntity.ok(ableBookingDateRoundResponse);
	}
}
