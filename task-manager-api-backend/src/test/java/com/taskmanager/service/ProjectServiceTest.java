package com.taskmanager.service;

import com.taskmanager.dto.project.CreateProjectRequest;
import com.taskmanager.dto.project.ProjectResponse;
import com.taskmanager.dto.project.UpdateProjectRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.ProjectMapper;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User owner;
    private User other;
    private Project project;
    private ProjectResponse projectResponse;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@test.com").name("Owner").password("pass").build();
        other = User.builder().id(2L).email("other@test.com").name("Other").password("pass").build();

        project = Project.builder()
                .id(1L).name("My Project").description("Desc")
                .status(ProjectStatus.ACTIVE).user(owner)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

        projectResponse = new ProjectResponse(1L, "My Project", "Desc",
                ProjectStatus.ACTIVE, project.getCreatedAt(), project.getUpdatedAt());
    }

    @Test
    void listProjects_ReturnsOnlyOwnerProjects() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByUser(owner, Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(project)));
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        var result = projectService.listProjects(owner.getEmail(), null, Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("My Project");
    }

    @Test
    void listProjects_WithStatusFilter_FiltersCorrectly() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByUserAndStatus(owner, ProjectStatus.ACTIVE, Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(project)));
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        var result = projectService.listProjects(owner.getEmail(), ProjectStatus.ACTIVE, Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        verify(projectRepository).findByUserAndStatus(owner, ProjectStatus.ACTIVE, Pageable.unpaged());
    }

    @Test
    void createProject_SavesAndReturnsResponse() {
        var request = new CreateProjectRequest("New Project", "Desc");

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(projectResponse);

        var result = projectService.createProject(request, owner.getEmail());

        assertThat(result.name()).isEqualTo("My Project");
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void updateProject_WithOwner_UpdatesFields() {
        var request = new UpdateProjectRequest("Updated Name", "Updated Desc");
        var updated = new ProjectResponse(1L, "Updated Name", "Updated Desc",
                ProjectStatus.ACTIVE, project.getCreatedAt(), LocalDateTime.now());

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(1L, owner)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(updated);

        var result = projectService.updateProject(1L, request, owner.getEmail());

        assertThat(result.name()).isEqualTo("Updated Name");
    }

    @Test
    void getProject_WhenProjectBelongsToAnotherUser_ThrowsForbidden() {
        when(userRepository.findByEmail(other.getEmail())).thenReturn(Optional.of(other));
        when(projectRepository.findByIdAndUser(1L, other)).thenReturn(Optional.empty());
        when(projectRepository.existsById(1L)).thenReturn(true);

        assertThatThrownBy(() -> projectService.getProject(1L, other.getEmail()))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void getProject_WhenProjectDoesNotExist_ThrowsNotFound() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(99L, owner)).thenReturn(Optional.empty());
        when(projectRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> projectService.getProject(99L, owner.getEmail()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void archiveProject_SetsStatusArchived() {
        var archived = new ProjectResponse(1L, "My Project", "Desc",
                ProjectStatus.ARCHIVED, project.getCreatedAt(), LocalDateTime.now());

        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(1L, owner)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(archived);

        var result = projectService.archiveProject(1L, owner.getEmail());

        assertThat(result.status()).isEqualTo(ProjectStatus.ARCHIVED);
    }

    @Test
    void deleteProject_CallsRepositoryDelete() {
        when(userRepository.findByEmail(owner.getEmail())).thenReturn(Optional.of(owner));
        when(projectRepository.findByIdAndUser(1L, owner)).thenReturn(Optional.of(project));

        projectService.deleteProject(1L, owner.getEmail());

        verify(projectRepository).delete(project);
    }
}
