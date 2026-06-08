package com.workout.taskmanager.security.service;

import com.workout.taskmanager.security.entity.RefreshToken;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.security.repository.RefreshTokenRepository;
import com.workout.taskmanager.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final UserRepository userRepository;



    @Value("${jwt.refresh-expiration}")
    private Long refreshExpirationMs;
    public RefreshTokenService(RefreshTokenRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        Optional<RefreshToken> existing =
                repository.findByUser(user);

        if (existing.isPresent()) {

            RefreshToken token = existing.get();

            token.setToken(UUID.randomUUID().toString());

            token.setExpiryDate(
                    Instant.now().plusMillis(refreshExpirationMs)
            );

            return repository.save(token);
        }

        RefreshToken token = new RefreshToken();

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());

        token.setExpiryDate(
                Instant.now().plusMillis(refreshExpirationMs)
        );

        return repository.save(token);
    }

    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken =
                repository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}