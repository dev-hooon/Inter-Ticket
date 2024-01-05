package dev.hooon.show.application.facade;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hooon.show.application.PeriodType;
import dev.hooon.show.application.RankingService;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;

@DisplayName("[RankingCacheFacade 테스트]")
@ExtendWith(MockitoExtension.class)
class RankingCacheFacadeTest {

	@InjectMocks
	private RankingCacheFacade rankingCacheFacade;
	@Mock
	private RankingService rankingService;
	@Mock
	private RedisTemplate<String, Object> redisTemplate;
	@Mock
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("[캐시에 해당 카테고리, 집계기간에 대한 데이터가 있어서 캐시에서 데이터를 조회한다]")
	void getShowRankingWithCache_test_1() {
		//given
		RankingRequest rankingRequest = new RankingRequest("concert", "day");
		RankingResponse rankingResponse = new RankingResponse(
			PeriodType.DAY.getStartAt(),
			LocalDateTime.now(),
			new ArrayList<>()
		);

		// given(rankingService.getShowRanking(rankingRequest)).willReturn(rankingResponse);
		// redis 에 데이터가 있는 상황을 조성
		ValueOperations<String, Object> valueOperations = Mockito.mock(ValueOperations.class);
		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.get("R_concert_day")).willReturn(rankingResponse);
		given(objectMapper.convertValue(rankingResponse, RankingResponse.class)).willReturn(rankingResponse);

		//when
		RankingResponse result = rankingCacheFacade.getShowRankingWithCache(rankingRequest);

		//then
		assertThat(result).isEqualTo(rankingResponse);
	}

	@Test
	@DisplayName("[캐시에 해당 카테고리, 집계기간에 대한 데이터가 없어서 RankingService 에서 데이터를 조회한다]")
	void getShowRankingWithCache_test_2() {
		//given
		RankingRequest rankingRequest = new RankingRequest("concert", "day");
		RankingResponse rankingResponse = new RankingResponse(
			PeriodType.DAY.getStartAt(),
			LocalDateTime.now(),
			new ArrayList<>()
		);

		given(rankingService.getShowRanking(rankingRequest)).willReturn(rankingResponse);
		// redis 에 데이터가 없는 상황을 조성
		ValueOperations<String, Object> valueOperations = Mockito.mock(ValueOperations.class);
		given(redisTemplate.opsForValue()).willReturn(valueOperations);
		given(valueOperations.get("R_concert_day")).willReturn(null);

		//when
		RankingResponse result = rankingCacheFacade.getShowRankingWithCache(rankingRequest);

		//then
		assertThat(result).isEqualTo(rankingResponse);
		verify(valueOperations, times(1)).set(eq("R_concert_day"), eq(rankingResponse), any());
	}
}