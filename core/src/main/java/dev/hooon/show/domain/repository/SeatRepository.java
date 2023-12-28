package dev.hooon.show.domain.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.dto.query.SeatDateRoundDto;

public interface SeatRepository {

	void saveAll(Iterable<Seat> seats);

	Optional<Seat> findById(Long id);

	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);

	List<Seat> findByStatusIsCanceled();

	void updateStatusByIdIn(Collection<Long> ids, SeatStatus status);
}
