package dev.hooon.mail;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import dev.hooon.mail.dto.WaitingBookingMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailSender {

	private static final String PERSONAL = "Hooonterpark";
	private static final String TITLE = "[훈터파크] 예매대기 활성 알림";
	private static final String TEMPLATE = "waitingBookingNotification";

	private final String from;
	private final JavaMailSender mailSender;
	private final ITemplateEngine templateEngine;

	public MailSender(
		@Value("${spring.mail.username}")
		String from,
		JavaMailSender mailSender,
		ITemplateEngine templateEngine
	) {
		this.from = from;
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	public void sendWaitingBookingNotificationMail(WaitingBookingMailDto mailDto)
		throws MessagingException, UnsupportedEncodingException
	{
		Context context = new Context();
		context.setVariable("nickname", mailDto.nickname());
		context.setVariable("showName", mailDto.showName());

		String htmlTemplate = templateEngine.process(TEMPLATE, context);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setTo(mailDto.email());
		mimeMessageHelper.setFrom(new InternetAddress(from, PERSONAL));
		mimeMessageHelper.setSubject(TITLE);
		mimeMessageHelper.setText(htmlTemplate, true);

		mailSender.send(mimeMessage);
	}
}
