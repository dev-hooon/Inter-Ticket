package dev.hooon.common.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class)) // Adaptor 를 스캔목록에 추가하기 위한 코드
@AutoConfigureTestDatabase(replace = NONE)
public abstract class DataJpaTestSupport extends TestContainerSupport {
}
