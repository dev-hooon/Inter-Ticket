package dev.hooon.show.application;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.ShowMapper;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import dev.hooon.show.exception.ShowErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final SeatRepository seatRepository;

	private final ShowRepository showRepository;

	public AbleBookingDateRoundResponse getAbleBookingDateRoundInfo(Long showId) {
		List<SeatDateRoundDto> seatDateRoundDtoList = seatRepository.findSeatDateRoundInfoByShowId(showId);
		return ShowMapper.toSeatDateRoundResponse(seatDateRoundDtoList);
	}

	public ShowDetailsInfoResponse getShowDetailInfo(Long showId) {
		Show show = showRepository.findById(showId).orElseThrow(
			() -> new NotFoundException(ShowErrorCode.SHOW_NOT_FOUND)
		);
		return ShowMapper.toShowDetailInfoResponse(show);
	}
}
