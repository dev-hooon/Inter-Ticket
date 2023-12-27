package dev.hooon.show.dto.response.seats;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.hooon.show.domain.entity.seat.SeatStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SeatsDetailDto {

	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	private boolean isSeat;
	@JsonProperty("positionInfo_sector")
	private String sector;
	@JsonProperty("positionInfo_row")
	private String row;
	@JsonProperty("positionInfo_col")
	private int col;
	private int price;
	private SeatStatus isBookingAvailable;

	public SeatsDetailDto(Long id, LocalDate date, boolean isSeat, String sector, String row, int col, int price,
		SeatStatus isBookingAvailable) {
		this.id = id;
		this.date = date;
		this.isSeat = isSeat;
		this.sector = sector;
		this.row = row;
		this.col = col;
		this.price = price;
		this.isBookingAvailable = isBookingAvailable;
	}
}
