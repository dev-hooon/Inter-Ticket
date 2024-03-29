package dev.hooon.show.domain.entity.seat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatGrade {

	VIP("VIP석"),
	R("R석"),
	S("S석"),
	A("A석"),
	B("B석");

	private final String description;

	public String getDescription() {
		return description;
	}
}
