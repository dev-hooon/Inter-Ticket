package dev.hooon.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.auth.domain.entity.BlacklistToken;

public interface BlacklistJpaRepository extends JpaRepository<BlacklistToken, Long> {
	boolean existsByRefreshToken(String refreshToken);
}
