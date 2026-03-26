package com.taskmanager.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    static ApiError of(HttpStatus httpStatus, String message, String path) {
        return new ApiError(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                message,
                path
        );
    }
}
