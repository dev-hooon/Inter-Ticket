package dev.hooon.auth.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.hooon.auth.domain.entity.Auth;

@Repository
public interface AuthJpaRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByRefreshToken(String refreshToken);
}
