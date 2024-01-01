package dev.hooon.show.infrastructure.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
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

	List<Seat> findBySeatStatus(SeatStatus status);

	@Modifying
	@Query("update Seat s SET s.seatStatus = :status where s.id in :ids")
	void updateStatusByIdIn(@Param("ids") Collection<Long> ids, @Param("status") SeatStatus status);

	@Query("select show.name from Seat s left join Show show on show.id = s.show.id where s.id = :id")
	String findShowNameById(@Param("id") Long id);
}