package dev.hooon.show.dto.query.seats;

import java.time.LocalDate;
import java.util.Objects;

import dev.hooon.show.domain.entity.seat.SeatStatus;

public record SeatsDetailDto(
	Long id,
	LocalDate date,
	boolean isSeat,
	String sector,
	String row,
	int col,
	int price,
	SeatStatus isBookingAvailable

) {
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SeatsDetailDto that = (SeatsDetailDto)o;
		return isSeat == that.isSeat && col == that.col && price == that.price && Objects.equals(date, that.date)
			&& Objects.equals(sector, that.sector) && Objects.equals(row, that.row)
			&& isBookingAvailable == that.isBookingAvailable;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, isSeat, sector, row, col, price, isBookingAvailable);
	}
}
