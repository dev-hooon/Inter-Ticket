package dev.hooon.common.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
public abstract class IntegrationTestSupport extends TestContainerSupport {

	protected IntegrationTestSupport() {
	}
}
