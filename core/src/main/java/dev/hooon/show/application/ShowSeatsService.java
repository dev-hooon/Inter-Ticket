package dev.hooon.show.application;

import static dev.hooon.show.dto.SeatMapper.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;
import dev.hooon.show.dto.response.seats.SeatsInfoResponse;
import dev.hooon.show.dto.response.seats.ShowSeatsResponse;
import dev.hooon.show.exception.ShowErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowSeatsService {

	private final ShowRepository showRepository;
	private final SeatRepository seatRepository;

	public ShowSeatsResponse findShowSeatsByShowIdAndDateAndRound(Long showId, LocalDate date, int round) {

		throwIfShowDoesNotExist(showId);

		List<SeatsInfoDto> seatsInfoDtoList = seatRepository.findSeatInfoByShowIdAndDateAndRound(
			showId,
			date,
			round
		);

		List<SeatsInfoResponse> seatsInfoResponseList = toSeatsInfoResponse(seatsInfoDtoList);
		seatsInfoResponseList
			.forEach(it -> {
				List<SeatsDetailDto> seatsDetailDtoList = seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(
					showId,
					date,
					round,
					it.getGrade()
				);
				it.setSeats(getSeatsDetailResponses(seatsDetailDtoList));
			});

		return new ShowSeatsResponse(seatsInfoResponseList);
	}

	private void throwIfShowDoesNotExist(Long showId) {
		showRepository.findById(showId).orElseThrow(
			() -> new NotFoundException(ShowErrorCode.SHOW_NOT_FOUND)
		);
	}

}
