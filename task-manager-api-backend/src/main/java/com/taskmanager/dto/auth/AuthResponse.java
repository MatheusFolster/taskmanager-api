package com.taskmanager.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
}
