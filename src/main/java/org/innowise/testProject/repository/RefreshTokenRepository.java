package org.innowise.testProject.repository;

import org.innowise.testProject.model.RefreshToken;
import org.innowise.testProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
