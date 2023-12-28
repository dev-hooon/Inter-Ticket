package dev.hooon.waitingbooking.domain.entity;

import static dev.hooon.common.exception.CommonValidationError.*;
import static dev.hooon.waitingbooking.exception.WaitingBookingErrorCode.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "waiting_booking_table")
@NoArgsConstructor
public class WaitingBooking extends TimeBaseEntity {

	private static final String WAITING_BOOKING = "waitingBooking";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "waiting_booking_id")
	private Long id;

	@Column(name = "waiting_booking_status", nullable = false)
	private WaitingStatus status;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "waiting_booking_user_id",
		nullable = false,
		foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private User user;

	private int seatCount;

	private LocalDateTime expiredAt;

	@OneToMany(mappedBy = "waitingBooking", cascade = {REMOVE, PERSIST})
	List<WaitingBookingSeat> waitingBookingSeats = new ArrayList<>();

	// 생성 메소드
	private WaitingBooking(
		User user,
		int seatCount,
		List<Long> seatIds
	) {
		Assert.notNull(user, getNotNullMessage(WAITING_BOOKING, "user"));
		validateSeatCountInRange(seatCount);
		validateSelectedSeats(seatCount, seatIds);

		this.status = WaitingStatus.WAITING;
		this.seatCount = seatCount;
		this.user = user;
		applyWaitingBookingSeats(seatIds);
	}

	private void applyWaitingBookingSeats(List<Long> seatIds) {
		seatIds.forEach(seatId -> {
			WaitingBookingSeat waitingBookingSeat = WaitingBookingSeat.of(seatId, this);
			this.waitingBookingSeats.add(waitingBookingSeat);
		});
	}

	// 검증 메소드
	private void validateSeatCountInRange(int seatCount) {
		// 좌석 개수를 1~3 개 선택하지 않으면 예외
		if (seatCount < 1 || seatCount > 3) {
			throw new ValidationException(INVALID_SEAT_COUNT);
		}
	}

	private void validateSelectedSeats(int seatCount, List<Long> seatIds) {
		// 좌석을 하나도 고르지 않으면 예외
		if (seatIds.isEmpty()) {
			throw new ValidationException(EMPTY_SELECTED_SEAT);
		}

		// seatCount 보다 적게 선택하거나 10배수 넘게 좌석을 선택하면 예외
		int maxSeatCount = seatCount * 10;
		int selectedSeatCount = seatIds.size();
		if (selectedSeatCount > maxSeatCount || selectedSeatCount < seatCount) {
			throw new ValidationException(INVALID_SELECTED_SEAT_COUNT);
		}
	}

	// 팩토리 메소드
	public static WaitingBooking of(
		User user,
		int seatCount,
		List<Long> seatIds
	) {
		return new WaitingBooking(user, seatCount, seatIds);
	}

	public List<Long> getSelectedSeatIds() {
		return waitingBookingSeats.stream()
			.map(WaitingBookingSeat::getSeatId)
			.toList();
	}
}
