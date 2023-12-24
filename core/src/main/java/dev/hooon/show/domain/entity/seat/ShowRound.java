package dev.hooon.show.domain.entity.seat;

import static lombok.AccessLevel.*;

import java.time.LocalTime;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Convert(converter = Jsr310JpaConverters.LocalTimeConverter.class)
    private LocalTime startTime;

    public ShowRound(int round, LocalTime startTime) {
        this.round = round;
        this.startTime = startTime;
    }
}
