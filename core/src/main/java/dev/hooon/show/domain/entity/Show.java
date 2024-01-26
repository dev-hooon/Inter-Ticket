package dev.hooon.show.domain.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

import dev.hooon.common.entity.TimeBaseEntity;
import dev.hooon.show.domain.entity.place.Place;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "show_table", indexes = @Index(name = "idx_category", columnList = "show_category"))
@NoArgsConstructor
public class Show extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "show_id")
	private Long id;

	@Column(name = "show_name", nullable = false)
	private String name;

	@Enumerated(STRING)
	@Column(name = "show_category", nullable = false)
	private ShowCategory category;

	@Embedded
	private ShowPeriod showPeriod;

	@Embedded
	private ShowTime showTime;

	@Column(name = "show_age_limit", nullable = false)
	private String showAgeLimit;

	@Column(name = "show_total_seats")
	private int totalSeats;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "show_place_id", nullable = false, foreignKey = @ForeignKey(value = NO_CONSTRAINT))
	private Place place;

	public Show(
		String name,
		ShowCategory category,
		ShowPeriod showPeriod,
		ShowTime showTime,
		String showAgeLimit,
		int totalSeats,
		Place place
	) {
		this.name = name;
		this.category = category;
		this.showPeriod = showPeriod;
		this.showTime = showTime;
		this.showAgeLimit = showAgeLimit;
		this.totalSeats = totalSeats;
		this.place = place;
	}
}
