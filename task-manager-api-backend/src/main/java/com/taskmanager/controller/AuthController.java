package com.taskmanager.controller;

import com.taskmanager.dto.auth.AuthResponse;
import com.taskmanager.dto.auth.LoginRequest;
import com.taskmanager.dto.auth.RefreshTokenRequest;
import com.taskmanager.dto.auth.RegisterRequest;
import com.taskmanager.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, login and refresh token endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Creates a user account and returns JWT tokens")
    AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates credentials and returns JWT tokens")
    AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Exchanges a valid refresh token for new JWT tokens")
    AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }
}
