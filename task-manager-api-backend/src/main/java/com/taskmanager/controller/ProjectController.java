package com.taskmanager.controller;

import com.taskmanager.dto.project.CreateProjectRequest;
import com.taskmanager.dto.project.ProjectResponse;
import com.taskmanager.dto.project.UpdateProjectRequest;
import com.taskmanager.entity.enums.ProjectStatus;
import com.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Manage user projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "List projects", description = "Returns paginated projects of the authenticated user. Optionally filter by status.")
    Page<ProjectResponse> listProjects(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ProjectStatus status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return projectService.listProjects(userDetails.getUsername(), status, pageable);
    }

    @PostMapping
    @Operation(summary = "Create project", description = "Creates a new project for the authenticated user.")
    ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        ProjectResponse response = projectService.createProject(request, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    ProjectResponse getProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        return projectService.getProject(id, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates name and description of a project.")
    ProjectResponse updateProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request
    ) {
        return projectService.updateProject(id, request, userDetails.getUsername());
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archive project", description = "Sets project status to ARCHIVED.")
    ProjectResponse archiveProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        return projectService.archiveProject(id, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project", description = "Deletes project and all its tasks (cascade).")
    void deleteProject(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        projectService.deleteProject(id, userDetails.getUsername());
    }
}
