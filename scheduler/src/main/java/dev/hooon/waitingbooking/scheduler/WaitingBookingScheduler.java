package dev.hooon.waitingbooking.scheduler;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import dev.hooon.waitingbooking.application.facade.WaitingBookingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingBookingScheduler {

	private final WaitingBookingFacade waitingBookingFacade;
	private final Supplier<LocalDateTime> nowLocalDateTIme;

	@Scheduled(cron = "0 0/10 * * * *")
	@SchedulerLock(name = "wb_1", lockAtLeastFor = "9m", lockAtMostFor = "15m")
	public void scheduleWaitingBookingProcess() {
		log.info("wb_1 : run");
		waitingBookingFacade.processWaitingBooking();
	}

	@Scheduled(cron = "0/5 * * * * *")
	@SchedulerLock(name = "wb_2", lockAtLeastFor = "4s", lockAtMostFor = "8s")
	public void scheduleExpiredWaitingBookingProcess() {
		log.info("wb_2 : run");
		waitingBookingFacade.processExpiredWaitingBooking(nowLocalDateTIme.get());
	}
}
