package dev.hooon.user.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.hooon.user.domain.entity.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByName(String name);
	Long save(User user);

	Optional<User> findByEmail(String email);
}
