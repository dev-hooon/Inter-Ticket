package dev.hooon.show.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.dto.query.SeatDateRoundDto;

@DisplayName("[SeatRepository 테스트]")
@Sql("/sql/seat_dummy.sql") // Seat 에 대한 생성자가 없어서 save 를 직접 못해서 쿼리로 더미데이터 추가
class SeatRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private SeatRepository seatRepository;

	@Test
	@DisplayName("[show_id 로 좌석의 날짜, 회차, 시간 정보를 조회한다]")
	void findSeatDateRoundInfoByShowIdTest() {
		//when
		List<SeatDateRoundDto> result = seatRepository.findSeatDateRoundInfoByShowId(1L);

		//then
		assertThat(result).hasSize(2);
		SeatDateRoundDto seatInfo1 = result.get(0);
		SeatDateRoundDto seatInfo2 = result.get(1);
		assertAll(
			() -> assertThat(seatInfo1.showDate()).isEqualTo(seatInfo2.showDate()),
			() -> assertThat(seatInfo1.round()).isLessThan(seatInfo2.round()),
			() -> assertThat(seatInfo1.startTime()).isEqualTo(seatInfo2.startTime())
		);
	}
}