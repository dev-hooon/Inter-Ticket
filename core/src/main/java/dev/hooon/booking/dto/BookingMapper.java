package dev.hooon.booking.dto;

import java.util.List;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.dto.response.BookingCancelResponse;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import dev.hooon.booking.dto.response.TicketResponse;
import dev.hooon.booking.dto.response.TicketSeatResponse;
import dev.hooon.show.domain.entity.Show;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {

	public static TicketBookingResponse toTicketBookingResponse(Booking booking) {
		Show show = booking.getShow();
		List<TicketResponse> ticketResponseList = booking.getTickets().stream()
			.map(ticket -> new TicketResponse(
				ticket.getId(),
				show.getName(),
				ticket.getSeat().getSeatGrade().toString(),
				ticket.getSeat().getPositionInfo().toString(),
				ticket.getSeat().getSeatStatus().toString(),
				ticket.getSeat().getShowDate(),
				ticket.getSeat().getStartTime(),
				ticket.getSeat().getRound()
			))
			.toList();
		return new TicketBookingResponse(booking.getId(), ticketResponseList);
	}

	public static BookingCancelResponse toBookingCancelResponse(Booking booking) {
		List<TicketSeatResponse> ticketSeatResponseList = booking.getTickets().stream()
			.map(
				ticket -> new TicketSeatResponse(
					ticket.getSeat().getId(),
					ticket.getSeat().getSeatStatus().toString()
				)
			)
			.toList();
		return new BookingCancelResponse(
			booking.getBookingStatus().toString(),
			ticketSeatResponseList
		);
	}
}
