package com.workout.taskmanager.auth.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ConflictException;
import com.workout.taskmanager.common.exceptions.InvalidTokenException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.security.dto.LoginRequest;
import com.workout.taskmanager.security.dto.LogoutRequest;
import com.workout.taskmanager.security.dto.RefreshTokenRequest;
import com.workout.taskmanager.security.dto.RegisterRequest;
import com.workout.taskmanager.auth.dto.AuthResponse;
import com.workout.taskmanager.security.entity.RefreshToken;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.common.enums.Role;
import com.workout.taskmanager.security.repository.RefreshTokenRepository;
import com.workout.taskmanager.user.repository.UserRepository;
import com.workout.taskmanager.security.service.JwtService;
import com.workout.taskmanager.security.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Invalid email or password"));

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(
                accessToken,
                refreshToken.getToken()), "Successfully logged in")
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.USER);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(null, "User registered successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestBody @Valid RefreshTokenRequest request) {
        RefreshToken oldToken =
                refreshTokenService.verifyToken(
                        request.getRefreshToken()
                );
        // Rotate: revoke old token, issue new one
        RefreshToken newRefreshToken =
                refreshTokenService.rotateToken(oldToken);
        String accessToken =
                jwtService.generateToken(
                        newRefreshToken.getUser()
                );
        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(
                accessToken,
                newRefreshToken.getToken()),"Successfully refreshed token")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(request.getRefreshToken())
                        .orElseThrow(() -> new InvalidTokenException("Refresh token not found"));
        if (!refreshToken.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new AccessDeniedException("This refresh token does not belong to you");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully"));
    }
}
