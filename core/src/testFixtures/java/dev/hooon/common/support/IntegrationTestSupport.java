package dev.hooon.common.support;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
public abstract class IntegrationTestSupport extends TestContainerSupport {

	protected IntegrationTestSupport() {
	}

	@MockBean
	protected Supplier<LocalDateTime> nowLocalDateTime;
}
