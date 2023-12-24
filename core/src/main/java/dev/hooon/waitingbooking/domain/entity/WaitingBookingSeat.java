package dev.hooon.waitingbooking.domain.entity;

import static dev.hooon.common.exception.CommonValidationError.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import org.springframework.util.Assert;

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

	private static final String WAITING_BOOKING_SEAT = "waitingBookingSeat";

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

	// 생성 메소드
	private WaitingBookingSeat(Long seatId, WaitingBooking waitingBooking) {
		Assert.notNull(seatId, getNotNullMessage(WAITING_BOOKING_SEAT, "seatId"));
		Assert.notNull(waitingBooking, getNotNullMessage(WAITING_BOOKING_SEAT, "waitingBooking"));
		this.seatId = seatId;
		this.waitingBooking = waitingBooking;
	}

	// 팩토리 메소드
	public static WaitingBookingSeat of(Long seatId, WaitingBooking waitingBooking) {
		return new WaitingBookingSeat(seatId, waitingBooking);
	}
}
