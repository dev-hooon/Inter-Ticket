package dev.hooon.user.domain.repository;

import java.util.Optional;

import dev.hooon.user.domain.entity.User;

public interface UserRepository {

	Optional<User> findById(Long id);

	Optional<User> findByName(String name);
	Long save(User user);

	Optional<User> findByEmail(String email);
}
