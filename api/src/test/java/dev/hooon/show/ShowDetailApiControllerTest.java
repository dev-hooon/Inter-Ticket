package dev.hooon.show;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.support.ApiTestSupport;

@DisplayName("[ShowDetailApiController API 테스트]")
@Sql("/sql/show_dummy.sql")
class ShowDetailApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("[공연 아이디를 통해 API 를 호출하면 해당 공연의 세부 정보를 조회할 수 있다]")
	@Test
	void getShowDetailInfoTest() throws Exception {
		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/shows/1")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.showName").value("레미제라블"),
			jsonPath("$.showStartDate").value("2023-10-10"),
			jsonPath("$.showEndDate").value("2023-10-12"),
			jsonPath("$.showTime").value(150),
			jsonPath("$.showIntermissionTime").value(15),
			jsonPath("$.showAgeLimit").value("만 8세 이상"),

			jsonPath("$.placeDetailsInfo.showPlaceName").value("블루스퀘어 신한카드홀"),
			jsonPath("$.placeDetailsInfo.showPlaceContactInfo").value("1544-1591"),
			jsonPath("$.placeDetailsInfo.showPlaceAddress").value("서울특별시 용산구 이태원로 294 블루스퀘어(한남동)"),
			jsonPath("$.placeDetailsInfo.showPlaceUrl").value("http://www.bluesquare.kr/index.asp")
		);
	}
}
