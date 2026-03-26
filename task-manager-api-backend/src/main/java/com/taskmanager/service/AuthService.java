package com.taskmanager.service;

import com.taskmanager.dto.auth.AuthResponse;
import com.taskmanager.dto.auth.LoginRequest;
import com.taskmanager.dto.auth.RefreshTokenRequest;
import com.taskmanager.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);
}
