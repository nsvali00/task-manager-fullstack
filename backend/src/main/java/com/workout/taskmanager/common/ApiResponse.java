package com.workout.taskmanager.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private T data;
    private String message;
    private HttpStatus status;

    public ApiResponse() {
    }

    public ApiResponse(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(data, message, HttpStatus.CREATED);
    }
}
