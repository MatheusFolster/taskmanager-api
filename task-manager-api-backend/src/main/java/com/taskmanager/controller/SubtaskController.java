package com.taskmanager.controller;

import com.taskmanager.dto.subtask.CreateSubtaskRequest;
import com.taskmanager.dto.subtask.SubtaskResponse;
import com.taskmanager.service.SubtaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Subtasks", description = "Manage subtasks within tasks")
public class SubtaskController {

    private final SubtaskService subtaskService;

    @GetMapping("/tasks/{taskId}/subtasks")
    @Operation(summary = "List subtasks", description = "Returns all subtasks of a task in creation order.")
    List<SubtaskResponse> listSubtasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId
    ) {
        return subtaskService.listSubtasks(taskId, userDetails.getUsername());
    }

    @PostMapping("/tasks/{taskId}/subtasks")
    @Operation(summary = "Create subtask", description = "Creates a subtask inside a task.")
    ResponseEntity<SubtaskResponse> createSubtask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long taskId,
            @Valid @RequestBody CreateSubtaskRequest request
    ) {
        SubtaskResponse response = subtaskService.createSubtask(taskId, request, userDetails.getUsername());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/subtasks/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/subtasks/{id}/complete")
    @Operation(summary = "Complete subtask", description = "Marks a subtask as completed.")
    SubtaskResponse completeSubtask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        return subtaskService.completeSubtask(id, userDetails.getUsername());
    }

    @DeleteMapping("/subtasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete subtask")
    void deleteSubtask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        subtaskService.deleteSubtask(id, userDetails.getUsername());
    }
}
