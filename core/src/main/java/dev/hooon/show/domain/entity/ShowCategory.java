package dev.hooon.show.domain.entity;

import static dev.hooon.show.exception.ShowErrorCode.*;

import java.util.Arrays;

import dev.hooon.common.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShowCategory {

	MUSICAL("뮤지컬"),
	CONCERT("콘서트"),
	PLAY("연극");

	private final String description;

	public static ShowCategory of(String category) {
		return Arrays.stream(ShowCategory.values())
			.filter(showCategory -> showCategory.name().equalsIgnoreCase(category))
			.findAny()
			.orElseThrow(() -> new NotFoundException(SHOW_CATEGORY_NOT_FOUND));
	}
}
