package com.taskmanager.service;

import com.taskmanager.dto.subtask.CreateSubtaskRequest;
import com.taskmanager.dto.subtask.SubtaskResponse;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.Subtask;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubtaskMapper;
import com.taskmanager.repository.SubtaskRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.impl.SubtaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubtaskServiceTest {

    @Mock private SubtaskRepository subtaskRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private SubtaskMapper subtaskMapper;

    @InjectMocks
    private SubtaskServiceImpl subtaskService;

    private User owner;
    private User other;
    private Project project;
    private Task task;
    private Subtask subtask;
    private SubtaskResponse subtaskResponse;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@test.com").name("Owner").password("pass").build();
        other = User.builder().id(2L).email("other@test.com").name("Other").password("pass").build();

        project = Project.builder()
                .id(1L).name("Project").status(ProjectStatus.ACTIVE).user(owner)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        task = Task.builder()
                .id(1L).title("Task 1").status(TaskStatus.TODO).priority(TaskPriority.MEDIUM)
                .project(project).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        subtask = Subtask.builder()
                .id(1L).title("Subtask 1").completed(false)
                .task(task).createdAt(LocalDateTime.now())
                .build();

        subtaskResponse = new SubtaskResponse(1L, 1L, "Subtask 1", false, subtask.getCreatedAt());
    }

    @Test
    void listSubtasks_ReturnsSubtasksOfTask() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(1L, owner)).thenReturn(Optional.of(task));
        when(subtaskRepository.findByTaskOrderByCreatedAtAsc(task)).thenReturn(List.of(subtask));
        when(subtaskMapper.toResponse(subtask)).thenReturn(subtaskResponse);

        var result = subtaskService.listSubtasks(1L, owner.getEmail());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Subtask 1");
    }

    @Test
    void createSubtask_SavesAndReturnsResponse() {
        var request = new CreateSubtaskRequest("New Subtask");

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(1L, owner)).thenReturn(Optional.of(task));
        when(subtaskRepository.save(any(Subtask.class))).thenReturn(subtask);
        when(subtaskMapper.toResponse(subtask)).thenReturn(subtaskResponse);

        var result = subtaskService.createSubtask(1L, request, owner.getEmail());

        assertThat(result).isNotNull();
        verify(subtaskRepository).save(any(Subtask.class));
    }

    @Test
    void createSubtask_WithForbiddenTask_ThrowsForbiddenException() {
        var request = new CreateSubtaskRequest("New Subtask");

        when(userRepository.findByEmail(other.getEmail())).thenReturn(Optional.of(other));
        when(taskRepository.findByIdAndProjectUser(1L, other)).thenReturn(Optional.empty());
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertThatThrownBy(() -> subtaskService.createSubtask(1L, request, other.getEmail()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void createSubtask_WithNonExistentTask_ThrowsResourceNotFoundException() {
        var request = new CreateSubtaskRequest("New Subtask");

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(taskRepository.findByIdAndProjectUser(99L, owner)).thenReturn(Optional.empty());
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> subtaskService.createSubtask(99L, request, owner.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void completeSubtask_SetsCompletedTrue() {
        var completedSubtask = Subtask.builder()
                .id(1L).title("Subtask 1").completed(true)
                .task(task).createdAt(subtask.getCreatedAt())
                .build();
        var completedResponse = new SubtaskResponse(1L, 1L, "Subtask 1", true, subtask.getCreatedAt());

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(subtaskRepository.findById(1L)).thenReturn(Optional.of(subtask));
        when(subtaskRepository.save(any(Subtask.class))).thenReturn(completedSubtask);
        when(subtaskMapper.toResponse(completedSubtask)).thenReturn(completedResponse);

        var result = subtaskService.completeSubtask(1L, owner.getEmail());

        assertThat(result.completed()).isTrue();
        verify(subtaskRepository).save(subtask);
    }

    @Test
    void completeSubtask_FromAnotherUser_ThrowsForbiddenException() {
        Project otherProject = Project.builder()
                .id(2L).name("Other Project").status(ProjectStatus.ACTIVE).user(other)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        Task otherTask = Task.builder()
                .id(2L).title("Other Task").status(TaskStatus.TODO).priority(TaskPriority.LOW)
                .project(otherProject).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        Subtask otherSubtask = Subtask.builder()
                .id(2L).title("Other Subtask").completed(false)
                .task(otherTask).createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(subtaskRepository.findById(2L)).thenReturn(Optional.of(otherSubtask));

        assertThatThrownBy(() -> subtaskService.completeSubtask(2L, owner.getEmail()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void completeSubtask_WithNonExistentSubtask_ThrowsResourceNotFoundException() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(subtaskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subtaskService.completeSubtask(99L, owner.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteSubtask_RemovesSubtask() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(subtaskRepository.findById(1L)).thenReturn(Optional.of(subtask));

        subtaskService.deleteSubtask(1L, owner.getEmail());

        verify(subtaskRepository).delete(subtask);
    }
}
