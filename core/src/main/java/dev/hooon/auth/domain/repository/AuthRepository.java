package dev.hooon.auth.domain.repository;

import java.util.Optional;

import dev.hooon.auth.domain.entity.Auth;

public interface AuthRepository {
	Optional<Auth> findById(Long id);

	Optional<Auth> findByUserId(Long userId);

	Optional<Auth> findByRefreshToken(String refreshToken);

	Auth save(Auth auth);

	void updateRefreshToken(Long id, String refreshToken);
}
