package dev.hooon.show.domain.repository;

import java.util.List;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.dto.query.SeatDateRoundDto;

public interface SeatRepository {

	void saveAll(List<Seat> seats);
	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);
}
