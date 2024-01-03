package dev.hooon.show.infrastructure.adaptor;

import org.springframework.stereotype.Repository;

import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.PlaceRepository;
import dev.hooon.show.infrastructure.repository.PlaceJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryAdaptor implements PlaceRepository {

	private final PlaceJpaRepository placeJpaRepository;

	@Override
	public Place save(Place place) {
		return placeJpaRepository.save(place);
	}
}
