package dev.hooon.show.domain.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
