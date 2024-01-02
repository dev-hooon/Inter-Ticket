package dev.hooon.waitingbooking.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.hooon.waitingbooking.application.facade.WaitingBookingFacade;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingBookingScheduler {

	private final WaitingBookingFacade waitingBookingFacade;

	@Scheduled(cron = "0 0/10 * * * *")
	public void scheduleWaitingBookingProcess() {
		waitingBookingFacade.processWaitingBooking();
	}

	@Scheduled(cron = "0/5 * * * * *")
	public void scheduleExpiredWaitingBookingProcess() {
		waitingBookingFacade.processExpiredWaitingBooking();
	}
}
