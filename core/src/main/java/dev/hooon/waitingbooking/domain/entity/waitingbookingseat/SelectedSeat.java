package dev.hooon.waitingbooking.domain.entity.waitingbookingseat;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "selected_seat_table")
@DiscriminatorValue("selected")
public class SelectedSeat extends WaitingBookingSeat {

	private SelectedSeat(Long seatId, WaitingBooking waitingBooking) {
		super(seatId, waitingBooking);
	}

	// 팩토리 메소드
	public static SelectedSeat of(Long seatId, WaitingBooking waitingBooking) {
		return new SelectedSeat(seatId, waitingBooking);
	}
}
