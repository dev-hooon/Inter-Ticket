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
	private MockMvc mockMvc;
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
}
