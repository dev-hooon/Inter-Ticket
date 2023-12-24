package dev.hooon.show.infrastructure.adaptor;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.infrastructure.repository.ShowJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ShowRepositoryAdaptor implements ShowRepository {

	private final ShowJpaRepository showJpaRepository;

	@Override
	public Optional<Show> findById(Long id) {
		return showJpaRepository.findById(id);
	}

	@Override
	public Show save(Show show) {
		return showJpaRepository.save(show);
	}
}
