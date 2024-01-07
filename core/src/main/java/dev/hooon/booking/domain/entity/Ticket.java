package dev.hooon.booking.domain.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.seat.Seat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "ticket_table")
@NoArgsConstructor(access = PROTECTED)
public class Ticket extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ticket_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "ticket_seat_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private Seat seat;

	@ManyToOne
	@JoinColumn(name = "booking_id")
	private Booking booking;

	private Ticket(Seat seat) {
		this.seat = seat;
	}

	public static Ticket of(
		Seat seat
	) {
		return new Ticket(seat);
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public void markSeatStatusAsCanceled() {
		this.seat.markSeatStatusAsCanceled();
	}
}
