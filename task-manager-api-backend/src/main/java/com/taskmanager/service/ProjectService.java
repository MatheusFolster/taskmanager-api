package com.taskmanager.service;

import com.taskmanager.dto.project.CreateProjectRequest;
import com.taskmanager.dto.project.ProjectResponse;
import com.taskmanager.dto.project.UpdateProjectRequest;
import com.taskmanager.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Page<ProjectResponse> listProjects(String email, ProjectStatus status, Pageable pageable);

    ProjectResponse getProject(Long id, String email);

    ProjectResponse createProject(CreateProjectRequest request, String email);

    ProjectResponse updateProject(Long id, UpdateProjectRequest request, String email);

    ProjectResponse archiveProject(Long id, String email);

    void deleteProject(Long id, String email);
}
