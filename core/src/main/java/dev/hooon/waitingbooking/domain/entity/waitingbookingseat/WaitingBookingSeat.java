package dev.hooon.waitingbooking.domain.entity.waitingbookingseat;

import static dev.hooon.common.exception.CommonValidationError.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import org.springframework.util.Assert;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn
public abstract class WaitingBookingSeat {

	private static final String WAITING_BOOKING_SEAT = "waitingBookingSeat";

	@Id
	@GeneratedValue(strategy = AUTO)
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
	protected WaitingBookingSeat(Long seatId, WaitingBooking waitingBooking) {
		Assert.notNull(seatId, getNotNullMessage(WAITING_BOOKING_SEAT, "seatId"));
		Assert.notNull(waitingBooking, getNotNullMessage(WAITING_BOOKING_SEAT, "waitingBooking"));
		this.seatId = seatId;
		this.waitingBooking = waitingBooking;
	}
}

