package com.workout.taskmanager.common.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, String path) {
}
