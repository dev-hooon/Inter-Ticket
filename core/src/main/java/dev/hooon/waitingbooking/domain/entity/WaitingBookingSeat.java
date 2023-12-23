package dev.hooon.waitingbooking.domain.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

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
@Table(name = "waiting_booking_seat_table")
@NoArgsConstructor
public class WaitingBookingSeat {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "waiting_booking_seat_id")
	private Long id;

	@Column(name = "waiting_booking_seat_seat_id")
	private Long seatId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "waiting_booking_seat_waiting_booking_id",
		nullable = false,
		foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private WaitingBooking waitingBooking;
}
