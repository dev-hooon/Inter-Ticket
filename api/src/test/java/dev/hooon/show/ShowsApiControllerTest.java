package dev.hooon.show;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.domain.repository.ShowRepository;

@DisplayName("[ShowsApiController API 테스트]")
class ShowsApiControllerTest extends ApiTestSupport {

	@Autowired
	private ShowRepository showRepository;
	@Autowired
	private PlaceRepository placeRepository;

	@DisplayName("[올바른 공연 목록이 반환된다]")
	@Test
	void getShowsByCategoryTest() throws Exception {
		// Given
		Place place = TestFixture.getPlace();
		placeRepository.save(place);
		String placeName = place.getName();
		Show show1 = TestFixture.getShow(place, "레미제라블", ShowCategory.MUSICAL);
		Show show2 = TestFixture.getShow(place, "서울의 봄", ShowCategory.PLAY);
		Show show3 = TestFixture.getShow(place, "노량", ShowCategory.PLAY);
		showRepository.save(show1);
		showRepository.save(show2);
		showRepository.save(show3);

		int page = 0;
		int size = 3;
		String categoryPicked = "PLAY";

		// When
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/shows")
				.param("page", String.valueOf(page))
				.param("size", String.valueOf(size))
				.param("category", categoryPicked)
		);

		// Then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.size()").value(2),
			jsonPath("$[0].showName").value("서울의 봄"),
			jsonPath("$[0].placeName").value(placeName),
			jsonPath("$[1].showName").value("노량"),
			jsonPath("$[1].placeName").value(placeName)
		);
	}

	@Test
	@DisplayName("[0보다 작은 페이지 번호가 주어지면 400 오류를 반환한다]")
	void givenInvalidPage_whenGetShowsByCategory_thenReturnsBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/shows")
				.param("page", "-1")
				.param("size", "3")
				.param("category", "PLAY"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("[1보다 작은 사이즈가 주어지면 400 오류를 반환한다]")
	void givenInvalidSize_whenGetShowsByCategory_thenReturnsBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/shows")
				.param("page", "0")
				.param("size", "0")
				.param("category", "PLAY"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("[비어있는 String 이 카테고리로 주어지면 400 오류를 반환한다]")
	void givenInvalidCategory_whenGetShowsByCategory_thenReturnsBadRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/shows")
				.param("page", "0")
				.param("size", "3")
				.param("category", ""))
			.andExpect(status().isBadRequest());
	}

}
