package dev.hooon.show.application;

import static dev.hooon.show.domain.entity.ShowCategory.*;
import static dev.hooon.show.dto.response.RankingResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.ShowStatisticDto;
import dev.hooon.show.dto.request.RankingRequest;
import dev.hooon.show.dto.response.RankingResponse;

@DisplayName("[RankingService 테스트]")
@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

	@InjectMocks
	private RankingService rankingService;
	@Mock
	private ShowRepository showRepository;
	@Mock
	private Supplier<LocalDateTime> nowLocalDateTime;

	@Test
	@DisplayName("[카테고리, 집계기간 별 랭킹을 조회한다]")
	void getShowRanking_test() {
		//given
		RankingRequest rankingRequest = new RankingRequest("concert", "day");

		List<ShowStatisticDto> showStatistic = List.of(
			new ShowStatisticDto("showName1", "placeName1", LocalDate.of(2023, 11, 16), LocalDate.of(2023, 11, 20), 10),
			new ShowStatisticDto("showName2", "placeName2", LocalDate.of(2023, 11, 16), LocalDate.of(2023, 11, 20), 10)
		);

		given(showRepository.findShowStatistic(
			eq(CONCERT),
			any(LocalDateTime.class),
			any(LocalDateTime.class))
		).willReturn(showStatistic);

		LocalDateTime baseTime = LocalDateTime.of(2023, 11, 16, 12, 12);
		given(nowLocalDateTime.get()).willReturn(baseTime);

		//when
		RankingResponse result = rankingService.getShowRanking(rankingRequest);

		//then
		assertThat(result.aggregateStartAt()).isEqualTo(PeriodType.DAY.getStartAt(baseTime));
		assertThat(result.aggregateEndAt()).isEqualToIgnoringMinutes(baseTime);
		assertThat(result.showInfos()).hasSameSizeAs(showStatistic);

		for (int i = 0; i < showStatistic.size(); i++) {
			RankingShowInfo actual = result.showInfos().get(i);
			ShowStatisticDto expected = showStatistic.get(i);
			assertAll(
				() -> assertThat(actual.placeName()).isEqualTo(expected.placeName()),
				() -> assertThat(actual.showName()).isEqualTo(expected.showName()),
				() -> assertThat(actual.showStartDate()).isEqualTo(expected.showStartDate()),
				() -> assertThat(actual.showEndDate()).isEqualTo(expected.showEndDate()),
				() -> assertThat(actual.totalTicketCount()).isEqualTo(expected.totalTicketCount())
			);
		}
	}
}