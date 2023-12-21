package dev.hooon.show.domain.repository;

import java.util.List;

import dev.hooon.show.dto.query.SeatDateRoundDto;

public interface SeatRepository {

	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);
}
