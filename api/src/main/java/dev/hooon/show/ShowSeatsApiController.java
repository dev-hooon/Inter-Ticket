package dev.hooon.show;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.show.application.ShowSeatsService;
import dev.hooon.show.dto.response.seats.ShowSeatsResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowSeatsApiController {

	private final ShowSeatsService showSeatsService;

	@GetMapping("/api/shows/{showId}/seats")
	public ResponseEntity<ShowSeatsResponse> getShowSeatsInfo(
		@PathVariable("showId") Long showId,
		@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@RequestParam("round") int round
	) {
		ShowSeatsResponse showSeatsResponse = showSeatsService.findShowSeatsByShowIdAndDateAndRound(
			showId, date, round
		);
		return ResponseEntity.ok(showSeatsResponse);
	}

}
