package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowDetailApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows/{show_id}")
	public ResponseEntity<ShowDetailsInfoResponse> getShowDetailInfo(
		@PathVariable("show_id") Long showId
	) {
		ShowDetailsInfoResponse showDetailsInfoResponse = showService.getShowDetailInfo(showId);
		return ResponseEntity.ok(showDetailsInfoResponse);
	}
}
