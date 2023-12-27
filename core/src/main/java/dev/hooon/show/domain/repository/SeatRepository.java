package dev.hooon.show.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.seats.SeatsDetailDto;
import dev.hooon.show.dto.response.seats.SeatsInfoDto;

public interface SeatRepository {

	void saveAll(Iterable<Seat> seats);

	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);

	List<SeatsInfoDto> findSeatInfoByShowIdAndDateAndRound(
		@Param("showId") Long showId,
		@Param("date") LocalDate date,
		@Param("round") int round
	);

	List<SeatsDetailDto> findSeatsByShowIdAndDateAndRoundAndGrade(
		@Param("showId") Long showId,
		@Param("date") LocalDate date,
		@Param("round") int round,
		@Param("grade") SeatGrade grade
	);
}
