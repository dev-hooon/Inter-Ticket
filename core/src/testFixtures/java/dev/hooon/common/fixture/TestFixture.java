package dev.hooon.common.fixture;

import static dev.hooon.show.domain.entity.ShowCategory.*;
import static dev.hooon.show.domain.entity.seat.SeatGrade.*;
import static dev.hooon.show.domain.entity.seat.SeatStatus.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowPeriod;
import dev.hooon.show.domain.entity.ShowTime;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;

public class TestFixture {

	public static Place getPlace() {
		return new Place(
			"블루스퀘어 신한카드홀",
			"1544-1591",
			"서울특별시 용산구 이태원로 294 블루스퀘어(한남동)",
			"http://www.bluesquare.kr/index.asp"
		);
	}

	public static Show getShow(Place place) {
		ShowPeriod showPeriod = new ShowPeriod(LocalDate.of(2023, 10, 10), LocalDate.of(2023, 10, 12));
		ShowTime showTime = new ShowTime(150, 15);
		return new Show(
			"레미제라블",
			MUSICAL,
			showPeriod,
			showTime,
			"만 8세 이상",
			300,
			place
		);
	}

	public static List<Seat> getSeatList(Show show, LocalDate date, int round) {
		Seat vipSeat1 = Seat.of(show, VIP, true, "1층", "A", 2, 100000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		Seat vipSeat2 = Seat.of(show, VIP, true, "1층", "A", 3, 100000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		Seat sSeat1 = Seat.of(show, S, true, "2층", "A", 2, 70000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		Seat sSeat2 = Seat.of(show, S, true, "2층", "A", 3, 70000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		Seat aSeat = Seat.of(show, A, true, "3층", "A", 2, 50000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		return List.of(vipSeat1, vipSeat2, sSeat1, sSeat2, aSeat);
	}

	public static List<Seat> getVipSeatList(Show show, LocalDate date, int round) {
		Seat vipSeat1 = Seat.of(show, VIP, true, "1층", "A", 2, 100000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		Seat vipSeat2 = Seat.of(show, VIP, true, "1층", "A", 3, 100000, date, round, LocalTime.of(14, 0),
			AVAILABLE);
		return List.of(vipSeat1, vipSeat2);
	}

	public static List<SeatsDetailDto> seatListToSeatsDetailDto(List<Seat> seatList) {
		return seatList.stream()
			.map(it -> new SeatsDetailDto(
				it.getId(),
				it.getShowDate(),
				it.isSeat(),
				it.getPositionInfo().getSector(),
				it.getPositionInfo().getRow(),
				it.getPositionInfo().getCol(),
				it.getPrice(),
				it.getSeatStatus()
			))
			.toList();
	}
}
