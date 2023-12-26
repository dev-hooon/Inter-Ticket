package dev.hooon.show.domain.repository;

import java.util.Optional;

import dev.hooon.show.domain.entity.Show;

public interface ShowRepository {

	Optional<Show> findById(Long id);

	Show save(Show show);
}
