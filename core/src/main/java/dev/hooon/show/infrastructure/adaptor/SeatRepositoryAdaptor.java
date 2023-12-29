package dev.hooon.show.infrastructure.adaptor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;
import dev.hooon.show.infrastructure.repository.SeatJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryAdaptor implements SeatRepository {

	private final SeatJpaRepository seatJpaRepository;

	@Override
	public void saveAll(Iterable<Seat> seats) {
		seatJpaRepository.saveAll(seats);
	}

	@Override
	public List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId) {
		return seatJpaRepository.findSeatDateRoundInfoByShowId(showId);
	}

	@Override
	public List<SeatsInfoDto> findSeatInfoByShowIdAndDateAndRound(
		Long showId, LocalDate date, int round
	) {
		return seatJpaRepository.findSeatInfoByShowIdAndDateAndRound(showId, date, round);
	}

	@Override
	public List<SeatsDetailDto> findSeatsByShowIdAndDateAndRoundAndGrade(
		Long showId, LocalDate date, int round, SeatGrade grade
	) {
		return seatJpaRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, grade);
	}
}
