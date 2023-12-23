package dev.hooon.waitingbooking.domain.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import dev.hooon.common.entity.TimeBaseEntity;
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

	@OneToMany(mappedBy = "waitingBooking", cascade = {REMOVE, PERSIST})
	List<WaitingBookingSeat> waitingBookingSeats = new ArrayList<>();
}
