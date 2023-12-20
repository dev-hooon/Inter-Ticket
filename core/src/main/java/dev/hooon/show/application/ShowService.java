package dev.hooon.show.application;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.dto.ShowMapper;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowService {

	private final SeatRepository seatRepository;

	public AbleBookingDateRoundResponse getAbleBookingDateRoundInfo(Long showId) {
		List<SeatDateRoundDto> seatDateRoundDtoList = seatRepository.findSeatDateRoundInfoByShowId(showId);
		return ShowMapper.toSeatDateRoundResponse(seatDateRoundDtoList);
	}
}
