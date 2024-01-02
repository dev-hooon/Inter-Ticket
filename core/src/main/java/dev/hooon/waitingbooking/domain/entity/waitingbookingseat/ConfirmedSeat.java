package dev.hooon.waitingbooking.domain.entity.waitingbookingseat;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "confirmed_seat_table")
@DiscriminatorValue("confirmed")
public class ConfirmedSeat extends WaitingBookingSeat {

	private ConfirmedSeat(Long seatId, WaitingBooking waitingBooking) {
		super(seatId, waitingBooking);
	}

	public static ConfirmedSeat of(Long seatId, WaitingBooking waitingBooking) {
		return new ConfirmedSeat(seatId, waitingBooking);
	}
}
