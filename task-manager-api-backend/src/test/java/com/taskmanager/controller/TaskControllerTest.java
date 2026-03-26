package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.task.CreateTaskRequest;
import com.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.TaskRepository;
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

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired ProjectRepository projectRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtil jwtUtil;

    private String authHeader;
    private User owner;
    private Project project;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(User.builder()
                .email("owner@test.com").name("Owner")
                .password(passwordEncoder.encode("password123")).build());

        project = projectRepository.save(Project.builder()
                .name("Test Project").status(ProjectStatus.ACTIVE).user(owner).build());

        authHeader = "Bearer " + jwtUtil.generateAccessToken(owner.getEmail());
    }

    @Test
    void createTask_Returns201WithLocation() throws Exception {
        var request = new CreateTaskRequest("My Task", "desc", TaskPriority.HIGH, null, Set.of("backend"));

        mockMvc.perform(post("/projects/" + project.getId() + "/tasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("My Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.tags", hasItem("backend")));
    }

    @Test
    void createTask_WithBlankTitle_Returns400() throws Exception {
        var request = new CreateTaskRequest("", null, TaskPriority.LOW, null, null);

        mockMvc.perform(post("/projects/" + project.getId() + "/tasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listTasks_ReturnsTasksOfProject() throws Exception {
        taskRepository.save(Task.builder()
                .title("T1").status(TaskStatus.TODO).priority(TaskPriority.LOW).project(project).build());
        taskRepository.save(Task.builder()
                .title("T2").status(TaskStatus.DONE).priority(TaskPriority.HIGH).project(project).build());

        mockMvc.perform(get("/projects/" + project.getId() + "/tasks")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void listTasks_FilterByStatus_ReturnsFiltered() throws Exception {
        taskRepository.save(Task.builder()
                .title("Todo").status(TaskStatus.TODO).priority(TaskPriority.LOW).project(project).build());
        taskRepository.save(Task.builder()
                .title("Done").status(TaskStatus.DONE).priority(TaskPriority.LOW).project(project).build());

        mockMvc.perform(get("/projects/" + project.getId() + "/tasks?status=TODO")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Todo"));
    }

    @Test
    void updateTaskStatus_ChangesStatus() throws Exception {
        Task saved = taskRepository.save(Task.builder()
                .title("Task").status(TaskStatus.TODO).priority(TaskPriority.MEDIUM).project(project).build());

        var request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);

        mockMvc.perform(patch("/tasks/" + saved.getId() + "/status")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void deleteTask_Returns204() throws Exception {
        Task saved = taskRepository.save(Task.builder()
                .title("To Delete").status(TaskStatus.TODO).priority(TaskPriority.LOW).project(project).build());

        mockMvc.perform(delete("/tasks/" + saved.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTask_FromAnotherUser_Returns403() throws Exception {
        User other = userRepository.save(User.builder()
                .email("other@test.com").name("Other")
                .password(passwordEncoder.encode("pass")).build());
        Project otherProject = projectRepository.save(Project.builder()
                .name("Other Project").status(ProjectStatus.ACTIVE).user(other).build());
        Task otherTask = taskRepository.save(Task.builder()
                .title("Private").status(TaskStatus.TODO).priority(TaskPriority.LOW)
                .project(otherProject).build());

        mockMvc.perform(get("/tasks/" + otherTask.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isForbidden());
    }
}
