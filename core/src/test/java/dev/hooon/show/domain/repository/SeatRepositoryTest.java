package dev.hooon.show.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.dto.query.SeatDateRoundDto;

@DisplayName("[SeatJpaRepository 테스트]")
class SeatRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private SeatRepository seatRepository;

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
}