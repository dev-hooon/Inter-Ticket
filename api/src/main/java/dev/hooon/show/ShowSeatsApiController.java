package dev.hooon.show;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.show.application.SeatService;
import dev.hooon.show.application.ShowSeatsService;
import dev.hooon.show.dto.request.BookedSeatQueryRequest;
import dev.hooon.show.dto.response.ShowSeatResponse;
import dev.hooon.show.dto.response.seats.ShowSeatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Show API")
@RestController
@RequiredArgsConstructor
public class ShowSeatsApiController {

	private final ShowSeatsService showSeatsService;
	private final SeatService seatService;

	@NoAuth
	@GetMapping("/api/shows/{showId}/seats")
	@Operation(summary = "공연 예매할 날짜, 회차의 좌석정보 조회 API", description = "공연의 예매할 날짜, 회차에 해당하는 좌석 정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
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
	@Operation(summary = "공연 예매할 날짜, 회차의 예매된 좌석정보 조회 API", description = "공연의 예매할 날짜, 회차에 예매된 좌석정보를 조회한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<ShowSeatResponse> getBookedSeatInfo(
		@Valid @ModelAttribute BookedSeatQueryRequest request
	) {
		ShowSeatResponse bookedSeatsInfo = seatService.getBookedSeatsInfo(request);
		return ResponseEntity.ok(bookedSeatsInfo);
	}
}
