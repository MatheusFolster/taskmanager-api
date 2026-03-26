package com.taskmanager.service.impl;

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
import com.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponse> listProjects(String email, ProjectStatus status, Pageable pageable) {
        User user = findUser(email);
        Page<Project> projects = status != null
                ? projectRepository.findByUserAndStatus(user, status, pageable)
                : projectRepository.findByUser(user, pageable);
        return projects.map(projectMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id, String email) {
        User user = findUser(email);
        Project project = findProjectOwned(id, user);
        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, String email) {
        User user = findUser(email);
        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build();
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, UpdateProjectRequest request, String email) {
        User user = findUser(email);
        Project project = findProjectOwned(id, user);
        project.setName(request.name());
        project.setDescription(request.description());
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponse archiveProject(Long id, String email) {
        User user = findUser(email);
        Project project = findProjectOwned(id, user);
        project.setStatus(ProjectStatus.ARCHIVED);
        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id, String email) {
        User user = findUser(email);
        Project project = findProjectOwned(id, user);
        projectRepository.delete(project);
    }

    private Project findProjectOwned(Long id, User user) {
        return projectRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    if (projectRepository.existsById(id)) {
                        return new ForbiddenException("Access denied to project " + id);
                    }
                    return new ResourceNotFoundException("Project", id);
                });
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
