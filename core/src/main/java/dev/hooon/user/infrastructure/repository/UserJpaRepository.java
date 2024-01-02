package dev.hooon.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
