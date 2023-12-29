package dev.hooon.show.domain.repository;

import java.time.LocalDate;
import java.util.List;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;

public interface SeatRepository {

	void saveAll(Iterable<Seat> seats);

	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);

	List<SeatsInfoDto> findSeatInfoByShowIdAndDateAndRound(
		Long showId,
		LocalDate date,
		int round
	);

	List<SeatsDetailDto> findSeatsByShowIdAndDateAndRoundAndGrade(
		Long showId,
		LocalDate date,
		int round,
		SeatGrade grade
	);
}
