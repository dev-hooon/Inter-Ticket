package dev.hooon.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.common.dto.PagedResponse;
import dev.hooon.show.application.ShowService;
import dev.hooon.show.dto.response.ShowInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Tag(name = "Show API")
@RestController
@RequiredArgsConstructor
public class ShowsApiController {

	private final ShowService showService;

	@NoAuth
	@GetMapping("/api/shows")
	@Operation(summary = "카테고리별로 공연 전체조회 API", description = "선택한 카테고리의 전체 공연의 공연 이름, 장소 이름을 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<PagedResponse<ShowInfoResponse>> getShowsByCategory(
		@RequestParam("page") @Min(0) int page,
		@RequestParam("size") @Min(1) int size,
		@RequestParam("category") @NotBlank String category
	) {
		return ResponseEntity.ok(
			showService.getShowsByCategory(page, size, category)
		);
	}
}
