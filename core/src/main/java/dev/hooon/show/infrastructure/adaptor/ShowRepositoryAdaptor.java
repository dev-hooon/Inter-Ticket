package dev.hooon.show.infrastructure.adaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return showJpaRepository.findBookingStatisticByCategoryAndPeriod(category, startAt, endAt);
	}

	@Override
	public Page<Show> findByCategoryOrderByIdDesc(ShowCategory category, Pageable pageable) {
		return showJpaRepository.findByCategoryOrderByIdDesc(category, pageable);
	}

}
