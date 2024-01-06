package dev.hooon.show.infrastructure.adaptor;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.ShowStatisticDto;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;

@DisplayName("[ShowRepository 테스트]")
class ShowRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[show_id 로 해당 공연에 대한 정보를 조회할 수 있다]")
	void findByIdTest() {
		// given
		Place place = TestFixture.getPlace();
		Place savedPlace = placeRepository.save(place);

		Show show = TestFixture.getShow(savedPlace);
		Show savedShow = showRepository.save(show);

		// when
		Optional<Show> showOptional = showRepository.findById(savedShow.getId());

		// then
		Assertions.assertThat(showOptional).isNotEmpty();
		Show showResult = showOptional.get();
		assertAll(
			() -> assertThat(showResult.getName()).isNotEmpty(),
			() -> assertThat(showResult.getShowAgeLimit()).isNotEmpty(),
			() -> assertThat(showResult.getPlace().getName()).isNotEmpty()
		);
	}

	// 리플렉션을 통해 ticketCount 를 3개로 설정
	private void setTicketCount(Booking booking) {
		ReflectionTestUtils.setField(booking, "ticketCount", 3);
	}

	private void assertShowStatisticDto(ShowStatisticDto dto, Show expectedShow, int expectedTicketCount) {
		assertAll(
			() -> assertThat(dto.showName()).isEqualTo(expectedShow.getName()),
			() -> assertThat(dto.placeName()).isEqualTo(expectedShow.getPlace().getName()),
			() -> assertThat(dto.showStartDate()).isEqualTo(expectedShow.getShowPeriod().getStartDate()),
			() -> assertThat(dto.showEndDate()).isEqualTo(expectedShow.getShowPeriod().getEndDate()),
			() -> assertThat(dto.totalTicketCount()).isEqualTo(expectedTicketCount)
		);
	}

	@Test
	@DisplayName("[공연의 정보와 예매된 티켓의 수를 조회한다]")
	void findShowStatistic_test() {
		//given
		User user = User.ofBuyer("hello123@naver.com", "name", "password");
		userRepository.save(user);
		Place savedPlace = placeRepository.save(TestFixture.getPlace());

		List<Show> shows = List.of(
			TestFixture.getShow(savedPlace),
			TestFixture.getShow(savedPlace),
			TestFixture.getShow(savedPlace)
		);
		shows.forEach(show -> showRepository.save(show));

		List<Booking> bookings = List.of(
			Booking.of(user, shows.get(1)),
			Booking.of(user, shows.get(2)),
			Booking.of(user, shows.get(1))
		);
		bookings.forEach(this::setTicketCount);
		bookings.forEach(booking -> bookingRepository.save(booking));

		//when
		LocalDateTime startAt = LocalDateTime.now().minusDays(1);
		LocalDateTime endAt = LocalDateTime.now().plusDays(1);
		List<ShowStatisticDto> result = showRepository.findShowStatistic(
			shows.get(0).getCategory(),
			startAt,
			endAt
		);

		//then
		assertThat(result).hasSize(2);
		assertShowStatisticDto(result.get(0), shows.get(1), 6);
		assertShowStatisticDto(result.get(1), shows.get(2), 3);
	}
}
