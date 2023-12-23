package dev.hooon.user.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.hooon.user.domain.entity.User;

public interface UserRepository {

	Optional<User> findById(Long id);

	List<User> findByName(String name);
	Long save(User user);
}
