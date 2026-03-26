package com.taskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ApiError handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return ApiError.of(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ApiError handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {
        return ApiError.of(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ApiError handleBadCredentials(HttpServletRequest request) {
        return ApiError.of(HttpStatus.UNAUTHORIZED, "Invalid email or password", request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiError handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ApiError.of(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ApiError.of(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ApiError handleGeneric(HttpServletRequest request) {
        return ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request.getRequestURI());
    }
}
