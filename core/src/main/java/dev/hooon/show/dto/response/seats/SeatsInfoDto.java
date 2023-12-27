package dev.hooon.show.dto.response.seats;

import java.util.List;

import dev.hooon.show.domain.entity.seat.SeatGrade;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SeatsInfoDto {

	private SeatGrade grade;
	private Long leftSeats;
	private int price;
	private List<SeatsDetailDto> seats;

	public SeatsInfoDto(SeatGrade grade, Long leftSeats, int price) {
		this.grade = grade;
		this.leftSeats = leftSeats;
		this.price = price;
	}

	public void setSeats(List<SeatsDetailDto> seats) {
		this.seats = seats;
	}
}
