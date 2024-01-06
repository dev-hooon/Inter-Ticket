package dev.hooon.show.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.ShowMapper;
import dev.hooon.show.dto.query.ShowStatisticDto;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {

	private final ShowRepository showRepository;

	@Transactional(readOnly = true)
	public RankingResponse getShowRanking(RankingRequest rankingRequest) {
		ShowCategory category = ShowCategory.of(rankingRequest.category());
		LocalDateTime startAt = PeriodType.of(rankingRequest.period()).getStartAt();
		LocalDateTime endAt = LocalDateTime.now();

		List<ShowStatisticDto> showStatistic = showRepository.findShowStatistic(category, startAt, endAt);

		return ShowMapper.toRankingResponse(showStatistic, startAt, endAt);
	}
}
