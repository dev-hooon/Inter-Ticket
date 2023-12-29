package dev.hooon.show.infrastructure.repository;

import static dev.hooon.show.domain.entity.seat.SeatGrade.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;

@DisplayName("[SeatJpaRepository 테스트]")
class SeatJpaRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private SeatJpaRepository seatJpaRepository;

	@Autowired
	private PlaceJpaRepository placeJpaRepository;

	@Autowired
	private ShowJpaRepository showJpaRepository;

	@DisplayName("[showId, date, round 로 해당 공연 좌석에 대해 조회할 수 있다]")
	@Test
	void findSeatInfoByShowIdAndDateAndRoundTest() {
		// given
		Place place = TestFixture.getPlace();
		Place savedPlace = placeJpaRepository.save(place);

		Show show = TestFixture.getShow(savedPlace);
		Show savedShow = showJpaRepository.save(show);

		Long showId = savedShow.getId();
		LocalDate date = LocalDate.of(2024, 1, 1);
		int round = 2;

		List<Seat> seatList = TestFixture.getSeatList(savedShow, date, round);
		seatJpaRepository.saveAll(seatList);

		List<SeatsInfoDto> seatsIntroDetailDtoList = List.of(
			new SeatsInfoDto(VIP, 2L, 100000),
			new SeatsInfoDto(S, 2L, 70000),
			new SeatsInfoDto(A, 1L, 50000)
		);

		// when
		List<SeatsInfoDto> seatsIntroDetailDtoListResult = seatJpaRepository.findSeatInfoByShowIdAndDateAndRound(
			showId, date, round
		);

		// then
		assertThat(seatsIntroDetailDtoListResult).containsExactlyInAnyOrderElementsOf(seatsIntroDetailDtoList);
	}

	@DisplayName("[showId, date, round, grade 에 해당하는 이용가능한 좌석에 대해 조회할 수 있다]")
	@Test
	void findSeatsByShowIdAndDateAndRoundAndGradeTest() {
		// given
		Place place = TestFixture.getPlace();
		Place savedPlace = placeJpaRepository.save(place);

		Show show = TestFixture.getShow(savedPlace);
		Show savedShow = showJpaRepository.save(show);

		Long showId = savedShow.getId();
		LocalDate date = LocalDate.of(2024, 1, 1);
		int round = 2;

		List<Seat> seatList = TestFixture.getSeatList(savedShow, date, round);
		seatJpaRepository.saveAll(seatList);

		// when
		List<SeatsDetailDto> vipSeatsDetailResponse = seatJpaRepository.findSeatsByShowIdAndDateAndRoundAndGrade(
			showId,
			date,
			round,
			VIP
		);
		List<SeatsDetailDto> sSeatsDetailResponse = seatJpaRepository.findSeatsByShowIdAndDateAndRoundAndGrade(
			showId,
			date,
			round,
			S
		);
		List<SeatsDetailDto> aSeatsDetailResponse = seatJpaRepository.findSeatsByShowIdAndDateAndRoundAndGrade(
			showId,
			date,
			round,
			A
		);

		// then
		assertAll(
			() -> assertThat(vipSeatsDetailResponse.size()).isEqualTo(2),
			() -> assertThat(sSeatsDetailResponse.size()).isEqualTo(2),
			() -> assertThat(aSeatsDetailResponse.size()).isEqualTo(1)
		);
	}
}
