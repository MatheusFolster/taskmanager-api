package com.taskmanager.controller;

import com.taskmanager.dto.task.CreateTaskRequest;
import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.dto.task.UpdateTaskRequest;
import com.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.service.TaskService;
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
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Manage tasks within projects")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(summary = "List tasks", description = "Returns paginated tasks of a project. Supports filters: status, priority, tag.")
    Page<TaskResponse> listTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return taskService.listTasks(projectId, userDetails.getUsername(), status, priority, tag, pageable);
    }

    @PostMapping("/projects/{projectId}/tasks")
    @Operation(summary = "Create task", description = "Creates a task inside a project.")
    ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse response = taskService.createTask(projectId, request, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/tasks/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Get task by ID")
    TaskResponse getTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        return taskService.getTask(id, userDetails.getUsername());
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update task", description = "Updates all mutable fields of a task.")
    TaskResponse updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return taskService.updateTask(id, request, userDetails.getUsername());
    }

    @PatchMapping("/tasks/{id}/status")
    @Operation(summary = "Update task status", description = "Updates only the status of a task.")
    TaskResponse updateTaskStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return taskService.updateTaskStatus(id, request, userDetails.getUsername());
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete task")
    void deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        taskService.deleteTask(id, userDetails.getUsername());
    }
}
