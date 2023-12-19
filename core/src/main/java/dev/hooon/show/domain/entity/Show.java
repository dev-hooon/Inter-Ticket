package dev.hooon.show.domain.entity;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.place.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "show_table")
@NoArgsConstructor(access = PROTECTED)
public class Show extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "show_id")
    private Long id;

    @Embedded
    private ShowPeriod showPeriod;

    @Embedded
    private ShowTime showTime;

    @Column(name = "show_age", nullable = false)
    private String showAge;

    @Column(name = "show_total_seats")
    private int totalSeats;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "show_place_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Place place;
}
