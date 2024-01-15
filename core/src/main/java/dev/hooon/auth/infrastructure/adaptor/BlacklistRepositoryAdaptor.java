package dev.hooon.auth.infrastructure.adaptor;

import org.springframework.stereotype.Repository;

import dev.hooon.auth.domain.entity.BlacklistToken;
import dev.hooon.auth.domain.repository.BlacklistRepository;
import dev.hooon.auth.infrastructure.BlacklistJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BlacklistRepositoryAdaptor implements BlacklistRepository {

	private final BlacklistJpaRepository blacklistJpaRepository;

	@Override
	public boolean existsByRefreshToken(String refreshToken) {
		return blacklistJpaRepository.existsByRefreshToken(refreshToken);
	}

	@Override
	public void save(BlacklistToken blacklistToken) {
		blacklistJpaRepository.save(blacklistToken);
	}
}
