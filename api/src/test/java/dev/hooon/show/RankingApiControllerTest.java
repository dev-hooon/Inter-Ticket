package dev.hooon.show;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.fixture.UserFixture;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;

@DisplayName("[RankingApiController 테스트]")
class RankingApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ShowRepository showRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SeatRepository seatRepository;

	@Test
	@DisplayName("[카테고리, 일자 별 랭킹을 조회한다]")
	void getShowRanking_test() throws Exception {
		//given
		Place place = placeRepository.save(TestFixture.getPlace());
		Show show = TestFixture.getShow(place, "showName1", ShowCategory.CONCERT);
		showRepository.save(show);

		User user = UserFixture.getUser();
		userRepository.save(user);
		Seat seat = SeatFixture.getSeat(show);
		seatRepository.saveAll(List.of(seat));

		Booking booking = Booking.of(user, show);
		booking.addTicket(Ticket.of(seat));
		bookingRepository.save(booking);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/shows/ranking")
				.queryParam("category", "concert")
				.queryParam("period", "week")
		);

		//then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.aggregateStartAt").isNotEmpty(),
			jsonPath("$.aggregateEndAt").isNotEmpty(),
			jsonPath("$.showInfos.size()").value(1),
			jsonPath("$.showInfos[0].showName").value(show.getName()),
			jsonPath("$.showInfos[0].placeName").value(place.getName()),
			jsonPath("$.showInfos[0].showStartDate").value(show.getShowPeriod().getStartDate().toString()),
			jsonPath("$.showInfos[0].showEndDate").value(show.getShowPeriod().getEndDate().toString()),
			jsonPath("$.showInfos[0].totalTicketCount").value(1)
		);
	}
}