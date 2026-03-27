package com.taskmanager.dto.subtask;

import java.time.LocalDateTime;

public record SubtaskResponse(
        Long id,
        Long taskId,
        String title,
        boolean completed,
        LocalDateTime createdAt
) {
}
