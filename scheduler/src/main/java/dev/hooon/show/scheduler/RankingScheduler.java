package dev.hooon.show.scheduler;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import dev.hooon.show.application.PeriodType;
import dev.hooon.show.application.RankingService;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingScheduler {

	private final RankingService rankingService;
	private final RedisTemplate<String, Object> redisTemplate;

	private String generateCacheKey(RankingRequest rankingRequest) {
		return String.join("_", List.of("R", rankingRequest.category(), rankingRequest.period()));
	}

	@Scheduled(cron = "0 0 0/1 * * *")
	@SchedulerLock(name = "ranking_1", lockAtLeastFor = "10m", lockAtMostFor = "70m")
	public void cacheEvictRanking() {
		// category, periodType 을 통해 이중 반복문
		Arrays.stream(ShowCategory.values())
			.forEach(category -> Arrays.stream(PeriodType.values())
				.forEach(periodType -> {
					// category, periodType 별로 랭킹 조회
					RankingRequest rankingRequest = new RankingRequest(category.name(), periodType.name());
					RankingResponse rankingResponse = rankingService.getShowRanking(rankingRequest);

					// 조회한 랭킹을 redis 캐시에 등록
					String cacheKey = generateCacheKey(rankingRequest);
					redisTemplate.opsForValue().set(cacheKey, rankingResponse);
				}));
	}
}
