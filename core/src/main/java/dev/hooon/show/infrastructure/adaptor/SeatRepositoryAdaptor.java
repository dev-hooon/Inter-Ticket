package dev.hooon.show.infrastructure.adaptor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.domain.entity.seat.SeatStatus;
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
	public List<Seat> saveAll(Iterable<Seat> seats) {
		return seatJpaRepository.saveAll(seats);
	}

	@Override
	public Optional<Seat> findById(Long id) {
		return seatJpaRepository.findById(id);
	}

	@Override
	public List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId) {
		return seatJpaRepository.findSeatDateRoundInfoByShowId(showId);
	}

	@Override
	public List<Seat> findByStatusIsCanceled() {
		return seatJpaRepository.findBySeatStatus(SeatStatus.CANCELED);
	}

	@Override
	public void updateStatusByIdIn(Collection<Long> ids, SeatStatus status) {
		seatJpaRepository.updateStatusByIdIn(ids, status);
	}

	@Override
	public Optional<String> findShowNameById(Long id) {
		return seatJpaRepository.findShowNameById(id);
	}

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

	@Override
	public List<Seat> findByIdIn(List<Long> idList) {
		return seatJpaRepository.findWithShowByIdIn(idList);
	}

	@Override
	public List<SeatsDetailDto> findBookedSeatsByShowIdAndDateAndRound(
		Long showId,
		LocalDate date,
		int round
	) {
		return seatJpaRepository.findSeatsDetailByStatusAndDateAndRound(
			showId,
			SeatStatus.BOOKED,
			date,
			round
		);
	}
}
