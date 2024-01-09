package dev.hooon.show;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.NoAuth;
import dev.hooon.show.application.SeatService;
import dev.hooon.show.application.ShowSeatsService;
import dev.hooon.show.dto.request.BookedSeatQueryRequest;
import dev.hooon.show.dto.response.ShowSeatResponse;
import dev.hooon.show.dto.response.seats.ShowSeatsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowSeatsApiController {

	private final ShowSeatsService showSeatsService;
	private final SeatService seatService;

	@NoAuth
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

	@NoAuth
	@GetMapping("/api/shows/seats/booked")
	public ResponseEntity<ShowSeatResponse> getBookedSeatInfo(
		@Valid @ModelAttribute BookedSeatQueryRequest request
	) {
		ShowSeatResponse bookedSeatsInfo = seatService.getBookedSeatsInfo(request);
		return ResponseEntity.ok(bookedSeatsInfo);
	}
}
