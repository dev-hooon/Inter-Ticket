package dev.hooon.booking.domain.entity;

import static dev.hooon.booking.domain.entity.BookingStatus.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.user.domain.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
@Table(name = "booking_table")
@NoArgsConstructor(access = PROTECTED)
public class Booking extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "booking_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "booking_user_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "booking_show_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private Show show;

	@Enumerated(STRING)
	@Column(name = "booking_status", nullable = false)
	private BookingStatus bookingStatus;

	@Column(name = "booking_ticket_count", nullable = false)
	private int ticketCount = 0;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Ticket> tickets = new ArrayList<>();

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
		ticket.setBooking(this);
		this.ticketCount++;
	}

	private Booking(User user, Show show) {
		this.user = user;
		this.show = show;
		this.bookingStatus = BOOKED;
	}

	private Booking(User user, Show show, LocalDateTime localDateTime) {
		this.user = user;
		this.show = show;
		this.bookingStatus = BOOKED;
		this.createdAt = localDateTime;
	}

	public static Booking of(
		User user,
		Show show
	) {
		return new Booking(user, show);
	}

	public static Booking of(
		User user,
		Show show,
		LocalDateTime localDateTime
	) {
		return new Booking(user, show, localDateTime);
	}

	public void markBookingStatusAsCanceled() {
		this.bookingStatus = CANCELED;
	}

	public long getUserId() {
		return this.user.getId();
	}

	public String getShowName() {
		return this.getShow().getName();
	}

	public LocalDate getShowDate() {
		return getFirstTicket().getShowDate();
	}

	public int getRound() {
		return getFirstTicket().getRound();
	}

	public LocalTime getStartTime() {
		return getFirstTicket().getStartTime();
	}

	public Ticket getFirstTicket() {
		return this.getTickets().get(0);
	}

}
