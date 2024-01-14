package dev.hooon.auth.domain.repository;

import dev.hooon.auth.domain.entity.BlacklistToken;

public interface BlacklistRepository {

	boolean existsByRefreshToken(String refreshToken);

	void save(BlacklistToken blacklistToken);
}
