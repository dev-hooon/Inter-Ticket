package dev.hooon.mail;

import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import dev.hooon.mail.dto.WaitingBookingMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@DisplayName("[MailSender 테스트]")
class MailSenderTest {

	private final MailSender mailSender;
	private final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
	private final ITemplateEngine iTemplateEngine = Mockito.mock(ITemplateEngine.class);

	public MailSenderTest() {
		this.mailSender = new MailSender("hello123@naver.com", javaMailSender, iTemplateEngine);
	}

	@Test
	@DisplayName("[예약대기 활성화 알림 메일을 전송한다]")
	void sendWaitingBookingNotificationMail_test() throws MessagingException, UnsupportedEncodingException {
		//given
		given(iTemplateEngine.process(eq("waitingBookingNotification"), any(Context.class)))
			.willReturn("httpTemplate");

		MimeMessage mockMimeMessage = Mockito.mock(MimeMessage.class);
		given(javaMailSender.createMimeMessage()).willReturn(mockMimeMessage);

		WaitingBookingMailDto waitingBookingMailDto = new WaitingBookingMailDto("nickname", "hello123@naver.com",
			"showName");

		//when
		mailSender.sendWaitingBookingNotificationMail(waitingBookingMailDto);

		//then
		verify(javaMailSender, times(1)).send(mockMimeMessage);
	}
}