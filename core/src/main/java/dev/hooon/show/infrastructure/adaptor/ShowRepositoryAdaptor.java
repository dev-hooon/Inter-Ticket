package dev.hooon.show.infrastructure.adaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.ShowStatisticDto;
import dev.hooon.show.infrastructure.repository.ShowJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShowRepositoryAdaptor implements ShowRepository {

	private final ShowJpaRepository showJpaRepository;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Optional<Show> findById(Long id) {
		return showJpaRepository.findById(id);
	}

	@Override
	public Show save(Show show) {
		return showJpaRepository.save(show);
	}

	@Override
	public List<ShowStatisticDto> findShowStatistic(
		ShowCategory category,
		LocalDateTime startAt,
		LocalDateTime endAt
	) {
		String sql = """
			select straight_join show_name, place_name ,
			show_start_date, show_end_date, sum(booking_ticket_count) as total_ticket_count
			from show_table
			left join place_table p on place_id = show_place_id
			inner join booking_table b
			on booking_show_id = show_id
			and b.created_at between :start_at and :end_at
			where show_category = :category
			group by show_id
			order by sum(booking_ticket_count) desc
			""";

		RowMapper<ShowStatisticDto> rowMapper = (rs, rowNum) -> {
			String showName = rs.getString("show_name");
			String placeName = rs.getString("place_name");
			LocalDate showStartDate = rs.getDate("show_start_date").toLocalDate();
			LocalDate showEndDate = rs.getDate("show_end_date").toLocalDate();
			long totalTicketCount = rs.getLong("total_ticket_count");

			return new ShowStatisticDto(showName, placeName, showStartDate, showEndDate, totalTicketCount);
		};

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("start_at", startAt);
		paramMap.put("end_at", endAt);
		paramMap.put("category", category.name());
		return jdbcTemplate.query(sql, paramMap, rowMapper);
	}

	@Override
	public Page<Show> findByCategoryOrderByIdDesc(ShowCategory category, Pageable pageable) {
		return showJpaRepository.findByCategoryOrderByIdDesc(category, pageable);
	}

}
