package dev.hooon.booking.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TicketResponse(
	Long ticketId,
	String showName,
	String seatGrade,
	String seatPositionInfo,
	String seatStatus,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate showDate,

	@JsonFormat(pattern = "HH:mm:ss")
	LocalTime showTime,
	int showRound
) {

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TicketResponse that = (TicketResponse)o;
		return showRound == that.showRound && Objects.equals(showName, that.showName) && Objects.equals(
			seatGrade, that.seatGrade) && Objects.equals(seatPositionInfo, that.seatPositionInfo)
			&& Objects.equals(seatStatus, that.seatStatus) && Objects.equals(showDate, that.showDate)
			&& Objects.equals(showTime, that.showTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(showName, seatGrade, seatPositionInfo, seatStatus, showDate, showTime, showRound);
	}
}
