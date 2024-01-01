package dev.hooon.waitingbooking.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
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

	// 대기 상태인 WaitingBooking 조회
	public List<WaitingBooking> getWaitingBookingsByStatusIsWaiting() {
		return waitingBookingRepository.findByStatusIsWaiting();
	}

	// ID 에 해당하는 WaitingBooking ACTIVATION 상태로 변경하고 expireAt 6시간뒤로 설정
	@Transactional
	public void activateWaitingBooking(Long waitingBookingId) {
		waitingBookingRepository.updateToActiveById(waitingBookingId);
	}
}
