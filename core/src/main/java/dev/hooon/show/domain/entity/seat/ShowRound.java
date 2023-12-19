package dev.hooon.show.domain.entity.seat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class ShowRound {

    @Column(name = "seat_show_round")
    private int round;

    @Column(name = "seat_start_time")
    private LocalTime startTime;
}
