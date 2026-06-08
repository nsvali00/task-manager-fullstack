package com.workout.taskmanager.task.exceptions;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(Long id){
        super("Task with ID " +  id + " not found");
    }
}
