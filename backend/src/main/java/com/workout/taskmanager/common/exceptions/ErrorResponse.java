package com.workout.taskmanager.common.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class ErrorResponse {

    public LocalDateTime timestamp;
    public HttpStatus status;
    public String message;
    public String path;

    public ErrorResponse(LocalDateTime timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
