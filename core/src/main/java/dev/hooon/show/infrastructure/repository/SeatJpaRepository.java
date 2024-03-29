package dev.hooon.show.infrastructure.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatGrade;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

	@Query("""
		select distinct
		new dev.hooon.show.dto.query.SeatDateRoundDto(s.showDate, s.showRound.round, s.showRound.startTime)
		from Seat s
		where s.show.id = :showId
		order by s.showDate, s.showRound.round
		""")
	List<SeatDateRoundDto> findSeatDateRoundInfoByShowId(@Param("showId") Long showId);

	List<Seat> findBySeatStatus(SeatStatus status);

	@Modifying
	@Query("update Seat s SET s.seatStatus = :status where s.id in :ids")
	void updateStatusByIdIn(@Param("ids") Collection<Long> ids, @Param("status") SeatStatus status);

	@Query("select show.name from Seat s left join Show show on show.id = s.show.id where s.id = :id")
	Optional<String> findShowNameById(@Param("id") Long id);

	@Query("""
		select
		new dev.hooon.show.dto.query.seats.SeatsInfoDto(seat.seatGrade, COUNT(seat), seat.price)
		from Seat seat
		where seat.show.id= :showId and seat.showDate= :date and seat.showRound.round= :round
		group by seat.seatGrade, seat.price
		""")
	List<SeatsInfoDto> findSeatInfoByShowIdAndDateAndRound(
		@Param("showId") Long showId,
		@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@Param("round") int round
	);

	@Query("""
		select
		new dev.hooon.show.dto.query.seats.SeatsDetailDto(seat.id, seat.showDate, seat.isSeat, seat.positionInfo.sector, seat.positionInfo.row, seat.positionInfo.col, seat.price, seat.seatStatus)
		from Seat seat
		where seat.show.id = :showId and seat.showDate = :date and seat.showRound.round = :round and seat.seatGrade = :grade
		order by seat.id desc
		""")
	List<SeatsDetailDto> findSeatsByShowIdAndDateAndRoundAndGrade(
		@Param("showId") Long showId,
		@Param("date") LocalDate date,
		@Param("round") int round,
		@Param("grade") SeatGrade grade
	);

	@EntityGraph(attributePaths = "show")
	List<Seat> findWithShowByIdIn(List<Long> idList);

	@Query("""
		select new dev.hooon.show.dto.query.seats.SeatsDetailDto(
		s.id, s.showDate, s.isSeat, s.positionInfo.sector, s.positionInfo.row, s.positionInfo.col, s.price, s.seatStatus)
		from Seat s
		where s.show.id = :showId
		and s.seatStatus = :status
		and s.showDate = :date
		and s.showRound.round = :round
		order by s.id desc
		""")
	List<SeatsDetailDto> findSeatsDetailByStatusAndDateAndRound(
		@Param("showId") Long showId,
		@Param("status") SeatStatus status,
		@Param("date") LocalDate date,
		@Param("round") int round
	);
}
