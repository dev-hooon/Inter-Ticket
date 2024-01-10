package dev.hooon.show;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.ShowInfoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowsApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows/")
	public ResponseEntity<List<ShowInfoResponse>> getAbleBookingDateRoundInfo(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("category") String category
	) {
		List<ShowInfoResponse> responses = showService.getShowsByCategory(page, size, category);
		return ResponseEntity.ok(responses);
	}
}
