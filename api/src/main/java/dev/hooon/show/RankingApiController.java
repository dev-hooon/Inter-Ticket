package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.facade.RankingCacheFacade;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Ranking API")
@RestController
@RequiredArgsConstructor
public class RankingApiController {

	private final RankingCacheFacade rankingCacheFacade;

	@NoAuth
	@GetMapping("/api/shows/ranking")
	@Operation(summary = "랭킹 조회 API", description = "카테고리, 집계기간별 랭킹을 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<RankingResponse> getShowRanking(@Valid @ModelAttribute RankingRequest request) {
		RankingResponse rankingResponse = rankingCacheFacade.getShowRankingWithCache(request);
		return ResponseEntity.ok(rankingResponse);
	}
}
