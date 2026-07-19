package com.workout.taskmanager.common.exceptions;

public class IssueNotFoundException extends ResourceNotFoundException {

    public IssueNotFoundException(Long id) {
        super("Issue with ID " + id + " not found");
    }
}
