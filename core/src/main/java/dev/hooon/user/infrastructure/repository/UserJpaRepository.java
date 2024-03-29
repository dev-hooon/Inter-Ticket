package dev.hooon.user.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);

	Optional<User> findByEmail(String email);
}
