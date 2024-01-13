package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Show API")
@RestController
@RequiredArgsConstructor
public class ShowDetailApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows/{show_id}")
	@Operation(summary = "공연 상세정보 조회 API", description = "공연의 상세정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<ShowDetailsInfoResponse> getShowDetailInfo(
		@PathVariable("show_id") Long showId
	) {
		ShowDetailsInfoResponse showDetailsInfoResponse = showService.getShowDetailInfo(showId);
		return ResponseEntity.ok(showDetailsInfoResponse);
	}
}
