package dev.hooon.mail.event;

import java.io.UnsupportedEncodingException;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.mail.MailSender;
import dev.hooon.mail.dto.WaitingBookingMailDto;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.exception.ShowErrorCode;
import dev.hooon.waitingbooking.event.WaitingBookingActiveEvent;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WaitingBookingMailEventListener {

	private final MailSender mailSender;
	private final SeatRepository seatRepository;

	@Async("scheduler")
	@EventListener
	public void sendWaitingBookingMail(WaitingBookingActiveEvent event)
		throws MessagingException, UnsupportedEncodingException
	{
		String showName = seatRepository.findShowNameById(event.seatId())
			.orElseThrow(() -> new NotFoundException(ShowErrorCode.SHOW_NAME_NOT_FOUND));

		WaitingBookingMailDto mailDto = new WaitingBookingMailDto(
			event.nickname(),
			event.email(),
			showName
		);
		mailSender.sendWaitingBookingNotificationMail(mailDto);
	}
}
