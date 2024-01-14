package dev.hooon.common.support;

import org.springframework.boot.test.mock.mockito.MockBean;

import dev.hooon.mail.event.WaitingBookingMailEventListener;

public abstract class SchedulerTestSupport extends IntegrationTestSupport {

	@MockBean
	protected WaitingBookingMailEventListener eventListener;
}
