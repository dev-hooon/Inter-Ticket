package dev.hooon.show.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.show.domain.entity.place.Place;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {
}
