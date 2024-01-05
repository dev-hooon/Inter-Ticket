package dev.hooon.show.domain.entity;

import static dev.hooon.show.exception.ShowErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.hooon.common.exception.NotFoundException;

@DisplayName("[ShowCategory 테스트]")
class ShowCategoryTest {

	@Test
	@DisplayName("[입력된 카테고리 이름에 맞는 ShowCategory 를 응답한다]")
	void of_test_1() {
		//given
		String category = "play";

		//when
		ShowCategory result = ShowCategory.of(category);

		//then
		assertThat(result).isEqualTo(ShowCategory.PLAY);
	}

	@Test
	@DisplayName("[입력된 카테고리 이름에 맞는 ShowCategory 가 존재하지 않아 실패한다]")
	void of_test_2() {
		//given
		String category = "plays";

		//when, then
		assertThatThrownBy(() -> ShowCategory.of(category))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(SHOW_CATEGORY_NOT_FOUND.getMessage());
	}
}