package dev.hooon.show.application;

import java.util.List;
import java.util.Optional;

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
		Optional<Show> showOptional = showRepository.findById(showId);
		if (showOptional.isEmpty()) {
			throw new NotFoundException(ShowErrorCode.SHOW_NOT_FOUND);
		}
		return ShowMapper.toShowDetailInfoResponse(showOptional.get());
	}
}
