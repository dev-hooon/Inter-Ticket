package dev.hooon.show.domain.entity.place;

import static jakarta.persistence.GenerationType.*;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "place_table")
@NoArgsConstructor
@AllArgsConstructor
public class Place extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "place_id")
	private Long id;

	@Column(name = "place_name", nullable = false)
	private String name;

	@Column(name = "place_contact_info")
	private String contactInfo;

	@Column(name = "place_address", nullable = false)
	private String address;

	@Column(name = "place_url")
	private String placeUrl;
}
