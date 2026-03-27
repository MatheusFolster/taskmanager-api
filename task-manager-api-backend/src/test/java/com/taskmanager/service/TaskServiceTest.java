package com.taskmanager.service;

import com.taskmanager.dto.task.CreateTaskRequest;
import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;
    @Mock private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User owner;
    private Project project;
    private Task task;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@test.com").name("Owner").password("pass").build();

        project = Project.builder()
                .id(1L).name("Project").status(ProjectStatus.ACTIVE).user(owner)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        task = Task.builder()
                .id(1L).title("Task 1").status(TaskStatus.TODO).priority(TaskPriority.MEDIUM)
                .project(project).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        taskResponse = new TaskResponse(1L, 1L, "Task 1", null,
                TaskStatus.TODO, TaskPriority.MEDIUM, null, Set.of(),
                0, task.getCreatedAt(), task.getUpdatedAt());
    }

    @Test
    void listTasks_ReturnsPaginatedTasks() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(1L, owner)).thenReturn(Optional.of(project));
        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        var result = taskService.listTasks(1L, owner.getEmail(), null, null, null, Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Task 1");
    }

    @Test
    void createTask_SavesAndReturnsResponse() {
        var request = new CreateTaskRequest("New Task", null, TaskPriority.HIGH, null, null);

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(1L, owner)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(taskResponse);

        var result = taskService.createTask(1L, request, owner.getEmail());

        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTaskStatus_ChangesStatusCorrectly() {
        var request = new UpdateTaskStatusRequest(TaskStatus.IN_PROGRESS);
        var updatedResponse = new TaskResponse(1L, 1L, "Task 1", null,
                TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, null, Set.of(),
                task.getCreatedAt(), LocalDateTime.now());

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(1L, owner)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(updatedResponse);

        var result = taskService.updateTaskStatus(1L, request, owner.getEmail());

        assertThat(result.status()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void getTask_WhenTaskBelongsToAnotherUser_ThrowsForbidden() {
        var other = User.builder().id(2L).email("other@test.com").build();

        when(userRepository.findByEmail(other.getEmail())).thenReturn(Optional.of(other));
        when(taskRepository.findByIdAndProjectUser(1L, other)).thenReturn(Optional.empty());
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertThatThrownBy(() -> taskService.getTask(1L, other.getEmail()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void getTask_WhenTaskDoesNotExist_ThrowsNotFound() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(99L, owner)).thenReturn(Optional.empty());
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.getTask(99L, owner.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteTask_CallsRepositoryDelete() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(1L, owner)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, owner.getEmail());

        verify(taskRepository).delete(task);
    }
}
