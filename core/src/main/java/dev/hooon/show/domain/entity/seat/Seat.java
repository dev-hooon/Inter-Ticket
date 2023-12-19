package dev.hooon.show.domain.entity.seat;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.Show;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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

    @Column(name = "seat_is_booked", nullable = false)
    private boolean isBooked;
}
