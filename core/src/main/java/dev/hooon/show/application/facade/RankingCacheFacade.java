package dev.hooon.show.application.facade;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hooon.show.application.RankingService;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankingCacheFacade {

	private final RankingService rankingService;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	private String generateCacheKey(RankingRequest rankingRequest) {
		return String.join("_", List.of("R", rankingRequest.category(), rankingRequest.period()));
	}

	public RankingResponse getShowRankingWithCache(RankingRequest rankingRequest) {
		String cacheKey = generateCacheKey(rankingRequest);
		Object cacheValue = redisTemplate.opsForValue().get(cacheKey);

		// 캐시에 데이터가 있으면 캐시 데이터 리턴
		if (cacheValue != null) {
			return objectMapper.convertValue(cacheValue, RankingResponse.class);
		}

		// 케시에 데이터가 없으면 실제 로직 호출 후 캐싱
		RankingResponse rankingResponse = rankingService.getShowRanking(rankingRequest);
		redisTemplate.opsForValue().set(cacheKey, rankingResponse, Duration.ofMinutes(70));
		return rankingResponse;
	}
}
