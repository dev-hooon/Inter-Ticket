package dev.hooon.show.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShowCategory {

	MUSICAL("뮤지컬"),
	CONCERT("콘서트"),
	PLAY("연극");

	private final String description;
}
