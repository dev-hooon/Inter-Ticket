package dev.hooon.waitingbooking.application;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingBookingService {

	private final WaitingBookingRepository waitingBookingRepository;

	public WaitingBooking createWaitingBooking(User user, WaitingRegisterRequest request) {
		WaitingBooking waitingBooking = WaitingBooking.of(user, request.seatCount(), request.seatIds());
		waitingBookingRepository.save(waitingBooking);

		return waitingBooking;
	}

	// 대기 상태인 WaitingBooking 조회(fetch join selectedSeat + user)
	public List<WaitingBooking> getWaitingBookingsByStatusIsWaiting() {
		return waitingBookingRepository.findWithSelectedSeatsByStatus(WaitingStatus.WAITING);
	}

	// WaitingBooking 상태를 ACTIVATION 상태로 변경하고 expiredAt 6시간뒤로 설정한 후 확정 좌석정보를 저장
	@Transactional
	public void activateWaitingBooking(Long waitingBookingId, List<Long> confirmedSeatIds) {
		waitingBookingRepository.findById(waitingBookingId)
			.ifPresent(waitingBooking -> waitingBooking.toActive(confirmedSeatIds));
	}

	// 활성 상태인 WaitingBooking 조회(fetch join confirmedSeat)
	@Transactional(readOnly = true)
	public List<WaitingBooking> getWaitingBookingsByStatusIsActivation() {
		return waitingBookingRepository.findWithConfirmedSeatsByStatus(WaitingStatus.ACTIVATION);
	}

	// 활성 상태인 WaitingBooking 을 만료상태로 바꿈
	@Transactional
	public void expireActiveWaitingBooking(Collection<Long> targetIds) {
		waitingBookingRepository.updateStatusByIdIn(WaitingStatus.EXPIRED, targetIds);
	}
}
