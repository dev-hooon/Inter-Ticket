package dev.hooon.show.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowCategory;

public interface ShowJpaRepository extends JpaRepository<Show, Long> {

	Page<Show> findByCategoryOrderByIdDesc(ShowCategory category, Pageable pageable);

}
