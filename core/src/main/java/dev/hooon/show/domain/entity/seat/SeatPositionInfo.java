package dev.hooon.show.domain.entity.seat;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class SeatPositionInfo {

    @Column(name = "seat_sector")
    private String sector;

    @Column(name = "seat_row", nullable = false)
    private String row;

    @Column(name = "seat_col", nullable = false)
    private int col;

    public SeatPositionInfo(
        String sector,
        String row,
        int col
    ) {
        this.sector = sector;
        this.row = row;
        this.col = col;
    }
}
