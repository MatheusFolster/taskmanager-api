package com.taskmanager.dto.task;

import com.taskmanager.entity.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record UpdateTaskRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        String description,

        @NotNull(message = "Priority is required")
        TaskPriority priority,

        LocalDate dueDate,

        Set<String> tags
) {
}
