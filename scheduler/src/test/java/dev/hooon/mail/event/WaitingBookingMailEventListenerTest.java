package dev.hooon.mail.event;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.mail.MailSender;
import dev.hooon.mail.dto.WaitingBookingMailDto;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.exception.ShowErrorCode;
import dev.hooon.waitingbooking.event.WaitingBookingActiveEvent;
import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[WaitingBookingMailEventListener 테스트]")
class WaitingBookingMailEventListenerTest {

	@InjectMocks
	private WaitingBookingMailEventListener listener;
	@Mock
	private MailSender mailSender;
	@Mock
	private SeatRepository seatRepository;

	@Test
	@DisplayName("[예약대기 활성화 이벤트를 수신하여 예약대기 알림 이메일을 발송한다]")
	void sendWaitingBookingMail_test1() throws MessagingException, UnsupportedEncodingException {
		//given
		WaitingBookingActiveEvent event = new WaitingBookingActiveEvent("nickname", "hello123@naver.com", 1L);

		String showName = "showName";
		given(seatRepository.findShowNameById(event.seatId()))
			.willReturn(Optional.of(showName));

		//when
		listener.sendWaitingBookingMail(event);

		//then
		WaitingBookingMailDto waitingBookingMailDto = new WaitingBookingMailDto(
			event.nickname(),
			event.email(),
			showName
		);
		verify(mailSender, times(1)).sendWaitingBookingNotificationMail(waitingBookingMailDto);
	}

	@Test
	@DisplayName("[좌석 id 에 해당하는 공연이름이 존재하지 않아 실패한다]")
	void sendWaitingBookingMail_test2() {
		//given
		WaitingBookingActiveEvent event = new WaitingBookingActiveEvent("nickname", "hello123@naver.com", 1L);

		given(seatRepository.findShowNameById(event.seatId()))
			.willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> listener.sendWaitingBookingMail(event))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(ShowErrorCode.SHOW_NAME_NOT_FOUND.getMessage());
	}
}