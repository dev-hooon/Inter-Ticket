package dev.hooon.show.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.dto.query.ShowStatisticDto;

public interface ShowJpaRepository extends JpaRepository<Show, Long> {

	@Query("""
		select new dev.hooon.show.dto.query.ShowStatisticDto(
		s.name, p.name, s.showPeriod.startDate, s.showPeriod.endDate, sum(b.ticketCount))
		from Show s
		left join Place p on p.id = s.place.id
		inner join Booking b on s.id = b.show.id
		where s.category = :category
		and b.createdAt between :startAt and :endAt
		group by s.id, s.name, p.name, s.showPeriod.startDate, s.showPeriod.endDate
		order by sum(b.ticketCount) desc
		""")
	List<ShowStatisticDto> findBookingStatisticByCategoryAndPeriod(
		@Param("category") ShowCategory category,
		@Param("startAt") LocalDateTime startAt,
		@Param("endAt") LocalDateTime endAt
	);

	Page<Show> findByCategoryOrderByIdDesc(ShowCategory category, Pageable pageable);

}
