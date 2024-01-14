package dev.hooon.show.application;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.dto.SeatMapper;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.request.BookedSeatQueryRequest;
import dev.hooon.show.dto.response.ShowSeatResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatRepository seatRepository;

	// 취소 상태인 좌석의 ID 를 모두 조회
	public Set<Long> getCanceledSeatIds() {
		return seatRepository.findByStatusIsCanceled()
			.stream()
			.map(Seat::getId)
			.collect(Collectors.toSet());
	}

	// 좌석을 대기중 상태로 변경
	@Transactional
	public void updateSeatToWaiting(Collection<Long> targetIds) {
		seatRepository.updateStatusByIdIn(targetIds, SeatStatus.WAITING);
	}

	// 좌석을 대기중 예약가능 상태로 변경
	@Transactional
	public void updateSeatToAvailable(Collection<Long> targetIds) {
		seatRepository.updateStatusByIdIn(targetIds, SeatStatus.AVAILABLE);
	}

	@Transactional(readOnly = true)
	public List<Seat> findByIdIn(List<Long> ids) {
		return seatRepository.findByIdIn(ids);
	}

	public ShowSeatResponse getBookedSeatsInfo(BookedSeatQueryRequest request) {
		List<SeatsDetailDto> seatDetails = seatRepository.findBookedSeatsByShowIdAndDateAndRound(
			request.showId(),
			request.date(),
			request.round()
		);

		return SeatMapper.toShowSeatResponse(seatDetails);
	}
}
