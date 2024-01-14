package dev.hooon.show.scheduler;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.fixture.UserFixture;
import dev.hooon.common.support.IntegrationTestSupport;
import dev.hooon.show.application.PeriodType;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;

@DisplayName("[RankingScheduler 테스트]")
class RankingSchedulerTest extends IntegrationTestSupport {

	@Autowired
	private RankingScheduler rankingScheduler;
	@Autowired
	private ShowRepository showRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	@DisplayName("[모든 카테고리, 집계기간 별 랭킹 데이터를 캐시에 저장한다]")
	void cacheEvictRanking() {
		//given
		Place place = placeRepository.save(TestFixture.getPlace());
		List<Show> shows = List.of(
			TestFixture.getShow(place, "showName1", ShowCategory.CONCERT),
			TestFixture.getShow(place, "showName2", ShowCategory.PLAY),
			TestFixture.getShow(place, "showName2", ShowCategory.MUSICAL)
		);
		shows.forEach(show -> showRepository.save(show));

		User user = UserFixture.getUser();
		userRepository.save(user);

		List<Booking> bookings = List.of(
			Booking.of(user, shows.get(0), new ArrayList<>()),
			Booking.of(user, shows.get(1), new ArrayList<>()),
			Booking.of(user, shows.get(2), new ArrayList<>())
		);
		bookings.forEach(booking -> bookingRepository.save(booking));

		given(nowLocalDateTime.get()).willReturn(LocalDateTime.of(2023, 11, 16, 12, 12));

		//when
		rankingScheduler.cacheEvictRanking();

		//then
		Set<String> keys = redisTemplate.keys("R_*");
		int expectedKeySize = ShowCategory.values().length * PeriodType.values().length;
		assertThat(keys).hasSize(expectedKeySize);
	}
}