package dev.hooon.booking.domain.entity;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.seat.Seat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "ticket_table")
@NoArgsConstructor(access = PROTECTED)
public class Ticket extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ticket_booking_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Booking booking;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ticket_seat_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
    private Seat seat;
}
