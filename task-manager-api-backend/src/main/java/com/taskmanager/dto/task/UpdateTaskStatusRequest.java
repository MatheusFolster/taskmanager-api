package com.taskmanager.dto.task;

import com.taskmanager.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(

        @NotNull(message = "Status is required")
        TaskStatus status
) {
}
