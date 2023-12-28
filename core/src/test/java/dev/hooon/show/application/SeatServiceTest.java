package dev.hooon.show.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.repository.SeatRepository;

@DisplayName("[SeatService 테스트]")
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

	@InjectMocks
	private SeatService seatService;
	@Mock
	private SeatRepository seatRepository;

	@Test
	void getCanceledSeatIds() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(1L),
			SeatFixture.getSeat(2L),
			SeatFixture.getSeat(3L)
		);
		given(seatRepository.findByStatusIsCanceled())
			.willReturn(seats);

		//when
		Set<Long> result = seatService.getCanceledSeatIds();

		//then
		assertThat(result).hasSameSizeAs(seats);
		seats.forEach(seat -> assertThat(result).contains(seat.getId()));
	}
}