package dev.hooon.show.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.dto.query.ShowStatisticDto;

public interface ShowRepository {

	Optional<Show> findById(Long id);

	Show save(Show show);

	List<ShowStatisticDto> findShowStatistic(
		ShowCategory category,
		LocalDateTime startAt,
		LocalDateTime endAt
	);

	Page<Show> findByCategoryOrderByIdDesc(ShowCategory category, Pageable pageable);
}
