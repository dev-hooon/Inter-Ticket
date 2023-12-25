package dev.hooon.waitingbooking.application;

import org.springframework.stereotype.Service;

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
}
