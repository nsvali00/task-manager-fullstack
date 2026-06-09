package com.workout.taskmanager.task.exceptions;

public class InvalidTaskException extends RuntimeException{
    public InvalidTaskException(String message) {
        super(message);
    }
}
