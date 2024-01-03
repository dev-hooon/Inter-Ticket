package dev.hooon.auth.domain.repository;

import java.util.Optional;

import dev.hooon.auth.domain.entity.Auth;

public interface AuthRepository {
	Optional<Auth> findById(Long id);

	Auth save(Auth auth);

	Optional<Auth> findByRefreshToken(String refreshToken);
}
