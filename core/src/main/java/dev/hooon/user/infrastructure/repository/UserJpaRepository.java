package dev.hooon.user.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.user.domain.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	List<User> findByName(String name);
}
