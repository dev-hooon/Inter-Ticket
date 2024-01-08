package dev.hooon.show.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;
import dev.hooon.show.domain.entity.ShowPeriod;
import dev.hooon.show.domain.entity.ShowTime;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DisplayName("[SeatJpaRepository 테스트]")
class SeatRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private SeatRepository seatRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private ShowRepository showRepository;
	@PersistenceContext
	private EntityManager entityManager;

	private void assertSeatDateRoundDto(SeatDateRoundDto dto, Seat expected) {
		assertAll(
			() -> assertThat(dto.showDate()).isEqualTo(expected.getShowDate()),
			() -> assertThat(dto.round()).isEqualTo(expected.getRound()),
			() -> assertThat(dto.startTime()).isEqualTo(expected.getStartTime())
		);
	}

	@Test
	@DisplayName("[show_id 로 좌석의 (날짜, 회차, 시간) 정보를 중복없이 (날짜, 회차)로 정렬 조회한다]")
	void findSeatDateRoundInfoByShowIdTest() {
		//given
		LocalTime time = LocalTime.of(12, 34, 56);
		List<Seat> seats = List.of(
			SeatFixture.getSeat(LocalDate.now(), 1, time),
			SeatFixture.getSeat(LocalDate.now(), 1, time),
			SeatFixture.getSeat(LocalDate.now().plusDays(1), 1, time.plusHours(1)),
			SeatFixture.getSeat(LocalDate.now().plusDays(1), 2, time.plusHours(1))
		);
		seatRepository.saveAll(seats);

		//when
		List<SeatDateRoundDto> result = seatRepository.findSeatDateRoundInfoByShowId(1L);

		//then
		assertThat(result).hasSize(3);
		assertSeatDateRoundDto(result.get(0), seats.get(0));
		assertSeatDateRoundDto(result.get(1), seats.get(2));
		assertSeatDateRoundDto(result.get(2), seats.get(3));
	}

	@Test
	@DisplayName("[상태가 CANCELED 상태인 좌석을 조회한다]")
	void findByStatusIsCanceledTest() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(),
			SeatFixture.getSeat(),
			SeatFixture.getSeat()
		);
		// 0번 Seat 만 Canceled 상태로 변경(취소 기능이 아직 구현되지 않아서 리플렉션 사용)
		ReflectionTestUtils.setField(seats.get(0), "seatStatus", SeatStatus.CANCELED);

		seatRepository.saveAll(seats);

		//when
		List<Seat> result = seatRepository.findByStatusIsCanceled();

		//then
		assertThat(result)
			.hasSize(1)
			.contains(seats.get(0));
	}

	@Test
	@DisplayName("[id 와 status 를 입력하면 id 에 해당하는 좌석의 상태를 입력한 status 로 변경한다]")
	void updateStatusByIdInTest() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(),
			SeatFixture.getSeat(),
			SeatFixture.getSeat()
		);
		seatRepository.saveAll(seats);

		List<Long> seatIds = seats.stream().map(Seat::getId).toList();

		//when
		seatRepository.updateStatusByIdIn(seatIds, SeatStatus.CANCELED);
		entityManager.flush();
		entityManager.clear();

		//then
		List<Seat> actual = seatRepository.findByStatusIsCanceled();
		List<Long> actualIds = actual.stream().map(Seat::getId).toList();

		assertThat(actualIds)
			.hasSameSizeAs(seatIds)
			.containsAll(seatIds);
	}

	@Test
	@DisplayName("[id 에 해당하는 좌석의 공연 이름을 조회한다]")
	void findShowNameById_test() {
		//given
		Place place = new Place("placeName", null, "address", null);
		Show show = new Show(
			"show",
			ShowCategory.CONCERT,
			new ShowPeriod(LocalDate.now(), LocalDate.now().plusMonths(1)),
			new ShowTime(100, 10),
			"청불",
			100,
			place
		);

		placeRepository.save(place);
		showRepository.save(show);

		Seat seat = SeatFixture.getSeat(show);
		seatRepository.saveAll(List.of(seat));

		//when
		String result = seatRepository.findShowNameById(seat.getId()).orElseThrow();

		//then
		assertThat(result).isEqualTo(show.getName());
	}

	@Test
	@DisplayName("[id 리스트에 포함된 좌석들을 조회할 수 있다]")
	void findByIdInTest() {
		// given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(),
			SeatFixture.getSeat(),
			SeatFixture.getSeat()
		);
		seatRepository.saveAll(seats);

		List<Long> idList = seats.stream().map(Seat::getId).toList();

		// when
		List<Seat> seatList = seatRepository.findByIdIn(idList);

		// then
		assertEquals(seatList, seats);
	}

	@Test
	@DisplayName("[공연 id 와 공연날짜, 회차 정보로 예매된 좌석 정보를 조회한다]")
	void findBookedSeatsByShowIdAndDateAndRound_test() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(),
			SeatFixture.getSeat(),
			SeatFixture.getSeat()
		);
		seats.get(1).markSeatStatusAsBooked();
		seats.get(2).markSeatStatusAsBooked();
		seatRepository.saveAll(seats);

		List<SeatsDetailDto> expected = TestFixture.seatListToSeatsDetailDto(seats);

		//when
		List<SeatsDetailDto> result = seatRepository.findBookedSeatsByShowIdAndDateAndRound(
			1L,
			seats.get(0).getShowDate(),
			seats.get(0).getRound()
		);

		//then
		assertThat(result)
			.hasSize(2)
			.contains(expected.get(1), expected.get(2));
	}
}
