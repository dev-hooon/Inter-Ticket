package dev.hooon.show.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class ShowPeriod {

    @Column(name = "show_start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "show_end_date", nullable = false)
    private LocalDate endDate;
}
