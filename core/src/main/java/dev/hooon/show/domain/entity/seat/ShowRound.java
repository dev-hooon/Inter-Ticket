package dev.hooon.show.domain.entity.seat;

import static lombok.AccessLevel.*;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class ShowRound {

	@Column(name = "seat_show_round")
	private int round;

	@Column(name = "seat_start_time")
	private LocalTime startTime;

	public ShowRound(int round, LocalTime startTime) {
		this.round = round;
		this.startTime = startTime;
	}
}
