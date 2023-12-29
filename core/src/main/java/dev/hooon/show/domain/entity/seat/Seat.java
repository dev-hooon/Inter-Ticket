package dev.hooon.show.domain.entity.seat;

import static dev.hooon.common.exception.CommonValidationError.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.springframework.util.Assert;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.Show;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
@Table(name = "seat_table")
@NoArgsConstructor(access = PROTECTED)
public class Seat extends TimeBaseEntity {

	private static final String SEAT = "seat";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "seat_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "seat_show_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private Show show;

	@Enumerated(STRING)
	@Column(name = "seat_grade", nullable = false)
	private SeatGrade seatGrade;

	@Column(name = "seat_is_seat", nullable = false)
	private boolean isSeat;

	@Embedded
	private SeatPositionInfo positionInfo;

	@Column(name = "seat_price", nullable = false)
	private int price;

	@Column(name = "seat_show_date", nullable = false)
	private LocalDate showDate;

	@Embedded
	private ShowRound showRound;

	@Enumerated(STRING)
	@Column(name = "seat_status", nullable = false)
	private SeatStatus seatStatus;

	private Seat(
		Show show,
		SeatGrade seatGrade,
		boolean isSeat,
		String sector,
		String row,
		int col,
		int price,
		LocalDate showDate,
		int round,
		LocalTime startTime,
		SeatStatus seatStatus
	) {
		Assert.notNull(show, getNotNullMessage(SEAT, "show"));
		Assert.notNull(seatGrade, getNotNullMessage(SEAT, "seatGrade"));
		Assert.hasText(sector, getNotEmptyPostfix(SEAT, "sector"));
		Assert.hasText(row, getNotEmptyPostfix(SEAT, "row"));
		Assert.notNull(showDate, getNotNullMessage(SEAT, "showDate"));
		Assert.notNull(startTime, getNotNullMessage(SEAT, "startTime"));
		Assert.notNull(seatStatus, getNotNullMessage(SEAT, "seatStatus"));
		this.show = show;
		this.seatGrade = seatGrade;
		this.isSeat = isSeat;
		this.positionInfo = new SeatPositionInfo(sector, row, col);
		this.price = price;
		this.showDate = showDate;
		this.showRound = new ShowRound(round, startTime);
		this.seatStatus = seatStatus;
	}

	public static Seat of(
		Show show,
		SeatGrade seatGrade,
		boolean isSeat,
		String sector,
		String row,
		int col,
		int price,
		LocalDate showDate,
		int round,
		LocalTime startTime,
		SeatStatus seatStatus
	) {
		return new Seat(show, seatGrade, isSeat, sector, row, col, price, showDate, round, startTime, seatStatus);
	}

	public int getRound() {
		return showRound.getRound();
	}

	public LocalTime getStartTime() {
		return showRound.getStartTime();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Seat seat = (Seat)o;
		return isSeat == seat.isSeat && price == seat.price && Objects.equals(id, seat.id)
			&& Objects.equals(show, seat.show) && seatGrade == seat.seatGrade && Objects.equals(
			positionInfo, seat.positionInfo) && Objects.equals(showDate, seat.showDate)
			&& Objects.equals(showRound, seat.showRound) && seatStatus == seat.seatStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, show, seatGrade, isSeat, positionInfo, price, showDate, showRound, seatStatus);
	}
}
