package dev.hooon.show.domain.entity.seat;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.Show;
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
@Table(name = "seat_table")
@NoArgsConstructor(access = PROTECTED)
public class Seat extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seat_show_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Show show;

    @Enumerated(STRING)
    @Column(name = "seat_grade", nullable = false)
    private SeatGrade seatGrade;

    @Column(name = "seat_is_seat", nullable = false)
    private boolean isSeat;

    @Embedded
    private SeatPositionInfo positionInfo;

    @Column(name = "seat_price", nullable = false)
    private int price;

    @Column(name = "seat_show_date", nullable = false)
    private LocalDate showDate;

    @Embedded
    private ShowRound showRound;

    @Enumerated(STRING)
    @Column(name = "seat_status", nullable = false)
    private SeatStatus seatStatus;
}
