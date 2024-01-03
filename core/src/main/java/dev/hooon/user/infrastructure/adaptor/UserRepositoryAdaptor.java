package dev.hooon.user.infrastructure.adaptor;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdaptor implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}

}
