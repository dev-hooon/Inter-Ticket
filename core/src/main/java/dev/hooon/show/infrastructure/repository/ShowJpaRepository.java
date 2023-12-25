package dev.hooon.show.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.show.domain.entity.Show;

public interface ShowJpaRepository extends JpaRepository<Show, Long> {
}
