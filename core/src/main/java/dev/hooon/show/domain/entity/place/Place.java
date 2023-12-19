package dev.hooon.show.domain.entity.place;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "place_table")
@NoArgsConstructor(access = PROTECTED)
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
