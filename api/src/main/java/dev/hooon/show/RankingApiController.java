package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.facade.RankingCacheFacade;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RankingApiController {

	private final RankingCacheFacade rankingCacheFacade;

	@NoAuth
	@GetMapping("/api/shows/ranking")
	public ResponseEntity<RankingResponse> getShowRanking(@Valid @ModelAttribute RankingRequest request) {
		RankingResponse rankingResponse = rankingCacheFacade.getShowRankingWithCache(request);
		return ResponseEntity.ok(rankingResponse);
	}
}
