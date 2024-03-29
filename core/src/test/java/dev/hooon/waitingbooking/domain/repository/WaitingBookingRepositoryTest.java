package dev.hooon.waitingbooking.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.common.fixture.WaitingBookingFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.entity.UserRole;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DisplayName("[WaitingBookingRepository 테스트]")
class WaitingBookingRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private WaitingBookingRepository waitingBookingRepository;
	@Autowired
	private UserRepository userRepository;
	@PersistenceContext
	private EntityManager entityManager;

	private final User user = User.ofBuyer("hello123@naver.com", "name", "password");

	@BeforeEach
	void setUp() {
		userRepository.save(user);
	}

	@Test
	@DisplayName("[WaitingStatus 로 데이터를 최신순으로 조회한다]")
	void findByStatusIsWaitingTest() {
		//given
		List<WaitingBooking> waitingBookings = List.of(
			WaitingBookingFixture.getWaitingBooking(user),
			WaitingBookingFixture.getWaitingBooking(user),
			WaitingBookingFixture.getWaitingBooking(user)
		);
		// 0번 데이터만 상태를 ACTIVATION 으로 변경
		ReflectionTestUtils.setField(waitingBookings.get(0), "status", WaitingStatus.ACTIVATION);

		waitingBookings.forEach(waitingBooking -> waitingBookingRepository.save(waitingBooking));

		//when
		List<WaitingBooking> result = waitingBookingRepository.findWithSelectedSeatsByStatus(WaitingStatus.WAITING);

		//then
		assertThat(result)
			.hasSize(2)
			.containsExactly(waitingBookings.get(2), waitingBookings.get(1));
	}

	@Test
	@DisplayName("[id 에 해당하는 데이터의 status 를 ACTIVATION 으로 변경하고 expireAt 을 6시간뒤로 설정한다]")
	void updateToActiveByIdTest() {
		//given
		WaitingBooking waitingBooking = WaitingBookingFixture.getWaitingBooking(user);
		waitingBookingRepository.save(waitingBooking);

		//when
		waitingBookingRepository.updateToActiveById(waitingBooking.getId());
		entityManager.flush();
		entityManager.clear();

		//then
		WaitingBooking actual = waitingBookingRepository.findById(waitingBooking.getId()).orElseThrow();
		assertThat(actual.getStatus()).isEqualTo(WaitingStatus.ACTIVATION);
		assertThat(actual.getExpiredAt()).isEqualToIgnoringSeconds(LocalDateTime.now().plusHours(6));
	}

	@Test
	@DisplayName("[id 컬렉션에 포함되는 WaitingBooking 의 상태를 입력한 상태로 변경한다]")
	void updateStatusByIdIn_test() {
		//given
		List<WaitingBooking> waitingBookings = List.of(
			WaitingBookingFixture.getWaitingBooking(user),
			WaitingBookingFixture.getWaitingBooking(user)
		);
		waitingBookings.forEach(waitingBooking -> waitingBookingRepository.save(waitingBooking));
		List<Long> waitingBookingIds = waitingBookings.stream().map(WaitingBooking::getId).toList();

		//when
		waitingBookingRepository.updateStatusByIdIn(WaitingStatus.FIN, waitingBookingIds);
		entityManager.flush();
		entityManager.clear();

		//then
		List<WaitingBooking> all = waitingBookingRepository.findAll();

		assertThat(all).hasSize(2);
		all.forEach(waitingBooking -> assertThat(waitingBooking.getStatus()).isEqualTo(WaitingStatus.FIN));
	}
}
