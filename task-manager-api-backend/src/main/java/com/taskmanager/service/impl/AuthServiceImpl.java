package com.taskmanager.service.impl;

import com.taskmanager.config.JwtProperties;
import com.taskmanager.dto.auth.AuthResponse;
import com.taskmanager.dto.auth.LoginRequest;
import com.taskmanager.dto.auth.RefreshTokenRequest;
import com.taskmanager.dto.auth.RegisterRequest;
import com.taskmanager.entity.RefreshToken;
import com.taskmanager.entity.User;
import com.taskmanager.exception.TokenExpiredException;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtUtil;
import com.taskmanager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new TokenExpiredException("Refresh token not found or already used"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Refresh token expired");
        }

        refreshTokenRepository.delete(refreshToken);
        return buildAuthResponse(refreshToken.getUser());
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getEmail());

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtProperties.getAccessTokenExpiration()
        );
    }
}
