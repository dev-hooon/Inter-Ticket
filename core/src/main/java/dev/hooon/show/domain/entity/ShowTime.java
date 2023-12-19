package dev.hooon.show.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class ShowTime {

    @Column(name = "show_total_minutes", nullable = false)
    private int totalMinutes;

    @Column(name = "show_intermission_minutes", nullable = false)
    private int intermissionMinutes;
}
