package com.workout.taskmanager.security.repository;

import com.workout.taskmanager.security.entity.RefreshToken;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

}
