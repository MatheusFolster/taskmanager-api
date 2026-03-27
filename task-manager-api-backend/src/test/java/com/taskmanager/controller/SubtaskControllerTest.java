package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.dto.subtask.CreateSubtaskRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.Subtask;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.RefreshTokenRepository;
import com.taskmanager.repository.SubtaskRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubtaskControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired ProjectRepository projectRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired SubtaskRepository subtaskRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired JwtUtil jwtUtil;

    private String authHeader;
    private User owner;
    private Task task;

    @BeforeEach
    void setUp() {
        subtaskRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(User.builder()
                .email("owner@test.com").name("Owner")
                .password(passwordEncoder.encode("password123")).build());

        Project project = projectRepository.save(Project.builder()
                .name("Test Project").status(ProjectStatus.ACTIVE).user(owner).build());

        task = taskRepository.save(Task.builder()
                .title("Test Task").status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM).project(project).build());

        authHeader = "Bearer " + jwtUtil.generateAccessToken(owner.getEmail());
    }

    @Test
    void createSubtask_Returns201WithLocation() throws Exception {
        var request = new CreateSubtaskRequest("Write unit tests");

        mockMvc.perform(post("/tasks/" + task.getId() + "/subtasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("Write unit tests"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.taskId").value(task.getId()));
    }

    @Test
    void createSubtask_WithBlankTitle_Returns400() throws Exception {
        var request = new CreateSubtaskRequest("");

        mockMvc.perform(post("/tasks/" + task.getId() + "/subtasks")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listSubtasks_ReturnsSubtasksInOrder() throws Exception {
        subtaskRepository.save(Subtask.builder().title("First").completed(false).task(task).build());
        subtaskRepository.save(Subtask.builder().title("Second").completed(false).task(task).build());

        mockMvc.perform(get("/tasks/" + task.getId() + "/subtasks")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("First"))
                .andExpect(jsonPath("$[1].title").value("Second"));
    }

    @Test
    void completeSubtask_SetsCompletedTrue() throws Exception {
        Subtask saved = subtaskRepository.save(
                Subtask.builder().title("To Complete").completed(false).task(task).build());

        mockMvc.perform(patch("/subtasks/" + saved.getId() + "/complete")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteSubtask_Returns204() throws Exception {
        Subtask saved = subtaskRepository.save(
                Subtask.builder().title("To Delete").completed(false).task(task).build());

        mockMvc.perform(delete("/subtasks/" + saved.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTask_ReturnsProgressField() throws Exception {
        subtaskRepository.save(Subtask.builder().title("S1").completed(true).task(task).build());
        subtaskRepository.save(Subtask.builder().title("S2").completed(false).task(task).build());

        mockMvc.perform(get("/tasks/" + task.getId())
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progress").value(50));
    }

    @Test
    void createSubtask_OnTaskFromAnotherUser_Returns403() throws Exception {
        User other = userRepository.save(User.builder()
                .email("other@test.com").name("Other")
                .password(passwordEncoder.encode("pass")).build());
        String otherHeader = "Bearer " + jwtUtil.generateAccessToken(other.getEmail());

        var request = new CreateSubtaskRequest("Unauthorized subtask");

        mockMvc.perform(post("/tasks/" + task.getId() + "/subtasks")
                        .header("Authorization", otherHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
