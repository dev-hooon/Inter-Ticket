package dev.hooon.auth.infrastructure.adaptor;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.infrastructure.AuthJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryAdaptor implements AuthRepository {

	private final AuthJpaRepository authJpaRepository;

	@Override
	public Optional<Auth> findById(Long id) {
		return authJpaRepository.findById(id);
	}

	@Override
	public Auth save(Auth auth) {
		return authJpaRepository.save(auth);
	}

	@Override
	public Optional<Auth> findByRefreshToken(String refreshToken) {
		return authJpaRepository.findByRefreshToken(refreshToken);
	}

}
