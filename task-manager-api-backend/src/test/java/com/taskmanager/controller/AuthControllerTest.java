package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.auth.LoginRequest;
import com.taskmanager.dto.auth.RegisterRequest;
import com.taskmanager.entity.User;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void register_WithValidData_Returns201AndTokens() throws Exception {
        var request = new RegisterRequest("newuser@example.com", "New User", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void register_WithDuplicateEmail_Returns409() throws Exception {
        userRepository.save(User.builder()
                .email("existing@example.com")
                .name("Existing")
                .password(passwordEncoder.encode("password123"))
                .build());

        var request = new RegisterRequest("existing@example.com", "Another User", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already in use"));
    }

    @Test
    void register_WithInvalidEmail_Returns400() throws Exception {
        var request = new RegisterRequest("not-an-email", "User", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_WithShortPassword_Returns400() throws Exception {
        var request = new RegisterRequest("user@example.com", "User", "short");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithValidCredentials_Returns200AndTokens() throws Exception {
        userRepository.save(User.builder()
                .email("login@example.com")
                .name("Login User")
                .password(passwordEncoder.encode("password123"))
                .build());

        var request = new LoginRequest("login@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_WithWrongPassword_Returns401() throws Exception {
        userRepository.save(User.builder()
                .email("user@example.com")
                .name("User")
                .password(passwordEncoder.encode("correct-password"))
                .build());

        var request = new LoginRequest("user@example.com", "wrong-password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void login_WithUnknownEmail_Returns401() throws Exception {
        var request = new LoginRequest("unknown@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void protectedEndpoint_WithoutToken_Returns401() throws Exception {
        mockMvc.perform(post("/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_WithInvalidToken_Returns401() throws Exception {
        var request = new com.taskmanager.dto.auth.RefreshTokenRequest("invalid-token");

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
