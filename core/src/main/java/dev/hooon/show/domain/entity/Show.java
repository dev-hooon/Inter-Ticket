package dev.hooon.show.domain.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.place.Place;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "show_table")
@NoArgsConstructor
public class Show extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "show_id")
    private Long id;

    @Column(name = "show_name", nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "show_category", nullable = false)
    private ShowCategory category;

    @Embedded
    private ShowPeriod showPeriod;

    @Embedded
    private ShowTime showTime;

    @Column(name = "show_age_limit", nullable = false)
    private String showAgeLimit;

    @Column(name = "show_total_seats")
    private int totalSeats;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "show_place_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Place place;
}
