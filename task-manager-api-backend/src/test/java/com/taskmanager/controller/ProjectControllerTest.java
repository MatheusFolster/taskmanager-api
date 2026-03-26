package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.project.CreateProjectRequest;
import com.taskmanager.dto.project.UpdateProjectRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired ProjectRepository projectRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtil jwtUtil;

    private String authHeader;
    private User owner;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(User.builder()
                .email("owner@test.com")
                .name("Owner")
                .password(passwordEncoder.encode("password123"))
                .build());

        authHeader = "Bearer " + jwtUtil.generateAccessToken(owner.getEmail());
    }

    @Test
    void createProject_Returns201WithLocation() throws Exception {
        var request = new CreateProjectRequest("My Project", "Some description");

        mockMvc.perform(post("/projects")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("My Project"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createProject_WithBlankName_Returns400() throws Exception {
        var request = new CreateProjectRequest("", "desc");

        mockMvc.perform(post("/projects")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listProjects_ReturnsOnlyOwnerProjects() throws Exception {
        projectRepository.save(Project.builder()
                .name("P1").status(ProjectStatus.ACTIVE).user(owner).build());
        projectRepository.save(Project.builder()
                .name("P2").status(ProjectStatus.ARCHIVED).user(owner).build());

        mockMvc.perform(get("/projects")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void listProjects_FilterByStatus_ReturnsFiltered() throws Exception {
        projectRepository.save(Project.builder()
                .name("Active").status(ProjectStatus.ACTIVE).user(owner).build());
        projectRepository.save(Project.builder()
                .name("Archived").status(ProjectStatus.ARCHIVED).user(owner).build());

        mockMvc.perform(get("/projects?status=ACTIVE")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Active"));
    }

    @Test
    void getProject_WithValidId_ReturnsProject() throws Exception {
        Project saved = projectRepository.save(Project.builder()
                .name("My Project").status(ProjectStatus.ACTIVE).user(owner).build());

        mockMvc.perform(get("/projects/" + saved.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    void getProject_FromAnotherUser_Returns403() throws Exception {
        User other = userRepository.save(User.builder()
                .email("other@test.com").name("Other")
                .password(passwordEncoder.encode("pass")).build());

        Project otherProject = projectRepository.save(Project.builder()
                .name("Other Project").status(ProjectStatus.ACTIVE).user(other).build());

        mockMvc.perform(get("/projects/" + otherProject.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProject_WithOwner_ReturnsUpdated() throws Exception {
        Project saved = projectRepository.save(Project.builder()
                .name("Old Name").status(ProjectStatus.ACTIVE).user(owner).build());

        var request = new UpdateProjectRequest("New Name", "New Desc");

        mockMvc.perform(put("/projects/" + saved.getId())
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void archiveProject_SetsStatusArchived() throws Exception {
        Project saved = projectRepository.save(Project.builder()
                .name("Active Project").status(ProjectStatus.ACTIVE).user(owner).build());

        mockMvc.perform(patch("/projects/" + saved.getId() + "/archive")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVED"));
    }

    @Test
    void deleteProject_Returns204() throws Exception {
        Project saved = projectRepository.save(Project.builder()
                .name("To Delete").status(ProjectStatus.ACTIVE).user(owner).build());

        mockMvc.perform(delete("/projects/" + saved.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isNoContent());
    }

    @Test
    void anyEndpoint_WithoutToken_Returns401() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isUnauthorized());
    }
}
