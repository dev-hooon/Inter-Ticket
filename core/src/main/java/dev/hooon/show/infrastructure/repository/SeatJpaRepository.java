package dev.hooon.show.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.dto.query.SeatDateRoundDto;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

	@Query("""
		select distinct
		new dev.hooon.show.dto.query.SeatDateRoundDto(s.showDate, s.showRound.round, s.showRound.startTime)
		from Seat s
		where s.show.id = :showId
		order by s.showDate, s.showRound.round
		""")
	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(Long showId);
}
