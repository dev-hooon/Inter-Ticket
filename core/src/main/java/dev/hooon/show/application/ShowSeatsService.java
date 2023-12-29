package dev.hooon.show.application;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;
import dev.hooon.show.dto.response.seats.SeatsDetailResponse;
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
		List<SeatsInfoResponse> seatsInfoResponseList = seatsInfoDtoList.stream()
			.map(it -> new SeatsInfoResponse(it.grade(), it.leftSeats(), it.price()))
			.toList();
		seatsInfoResponseList
			.forEach(it -> {
				List<SeatsDetailDto> seatsDetailDtoList = seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(
					showId,
					date,
					round,
					it.getGrade()
				);
				List<SeatsDetailResponse> seatsDetailResponseList = seatsDetailDtoList.stream()
					.map(o -> new SeatsDetailResponse(
						o.id(),
						o.date(),
						o.isSeat(),
						o.sector(),
						o.row(),
						o.col(),
						o.price(),
						o.isBookingAvailable()
					))
					.collect(Collectors.toList());
				it.setSeats(seatsDetailResponseList);
			});

		return new ShowSeatsResponse(seatsInfoResponseList);
	}

	private void throwIfShowDoesNotExist(Long showId) {
		showRepository.findById(showId).orElseThrow(
			() -> new NotFoundException(ShowErrorCode.SHOW_NOT_FOUND)
		);
	}

}
