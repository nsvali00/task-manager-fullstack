package com.workout.taskmanager.security.service;

import com.workout.taskmanager.common.exceptions.InvalidTokenException;
import com.workout.taskmanager.security.entity.RefreshToken;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.security.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // Revoke any existing tokens for this user
        repository.revokeAllByUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        return repository.save(token);
    }

    /**
     * Verify token and implement reuse detection.
     * If a revoked token is presented, it means a previously rotated token was reused
     * (possible theft). Revoke ALL tokens for that user as a security measure.
     */
    @Transactional
    public RefreshToken verifyToken(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));

        // Reuse detection: if token is already revoked, someone is trying to use an old token
        if (refreshToken.isRevoked()) {
            log.warn("Refresh token reuse detected for user {}. Revoking all tokens.", refreshToken.getUser().getEmail());
            repository.revokeAllByUser(refreshToken.getUser());
            throw new InvalidTokenException("Refresh token has been revoked. Please login again.");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            repository.save(refreshToken);
            throw new InvalidTokenException("Refresh token expired");
        }

        return refreshToken;
    }

    /**
     * Rotate the refresh token: revoke the old one and issue a new one.
     * Called during /auth/refresh to ensure each token can only be used once.
     */
    @Transactional
    public RefreshToken rotateToken(RefreshToken oldToken) {
        // Revoke old token
        oldToken.setRevoked(true);
        repository.save(oldToken);

        // Issue new token
        RefreshToken newToken = new RefreshToken();
        newToken.setUser(oldToken.getUser());
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        return repository.save(newToken);
    }
}
