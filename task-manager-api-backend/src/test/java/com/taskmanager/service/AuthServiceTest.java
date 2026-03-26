package com.taskmanager.service;

import com.taskmanager.config.JwtProperties;
import com.taskmanager.dto.auth.LoginRequest;
import com.taskmanager.dto.auth.RefreshTokenRequest;
import com.taskmanager.dto.auth.RegisterRequest;
import com.taskmanager.entity.RefreshToken;
import com.taskmanager.entity.User;
import com.taskmanager.exception.TokenExpiredException;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtUtil;
import com.taskmanager.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .password("encoded-password")
                .build();
    }

    @Test
    void register_WithValidData_ReturnsAuthResponse() {
        var request = new RegisterRequest("test@example.com", "Test User", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(604800000L);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(86400000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        var response = authService.register(request);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(86400000L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithDuplicateEmail_ThrowsIllegalArgumentException() {
        var request = new RegisterRequest("existing@example.com", "User", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WithValidCredentials_ReturnsAuthResponse() {
        var request = new LoginRequest("test@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.password(), testUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("access-token");
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(604800000L);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(86400000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        var response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void login_WithWrongPassword_ThrowsBadCredentialsException() {
        var request = new LoginRequest("test@example.com", "wrong-password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(request.password(), testUser.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_WithUnknownEmail_ThrowsBadCredentialsException() {
        var request = new LoginRequest("unknown@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void refresh_WithValidToken_ReturnsNewAuthResponse() {
        var tokenValue = "valid-refresh-token";
        var request = new RefreshTokenRequest(tokenValue);
        var refreshToken = RefreshToken.builder()
                .token(tokenValue)
                .user(testUser)
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();

        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("new-access-token");
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(604800000L);
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(86400000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        var response = authService.refresh(request);

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    void refresh_WithExpiredToken_ThrowsTokenExpiredException() {
        var tokenValue = "expired-refresh-token";
        var request = new RefreshTokenRequest(tokenValue);
        var expiredToken = RefreshToken.builder()
                .token(tokenValue)
                .user(testUser)
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();

        when(refreshTokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("expired");

        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void refresh_WithInvalidToken_ThrowsTokenExpiredException() {
        var request = new RefreshTokenRequest("nonexistent-token");

        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(TokenExpiredException.class);
    }
}
