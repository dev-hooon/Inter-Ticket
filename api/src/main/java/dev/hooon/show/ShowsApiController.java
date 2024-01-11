package dev.hooon.show;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.ShowInfoResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowsApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows")
	public ResponseEntity<List<ShowInfoResponse>> getAbleBookingDateRoundInfo(
		@RequestParam("page") @Min(0) int page,
		@RequestParam("size") @Min(1) int size,
		@RequestParam("category") @NotBlank String category
	) {
		List<ShowInfoResponse> responses = showService.getShowsByCategory(page, size, category);
		return ResponseEntity.ok(responses);
	}
}
