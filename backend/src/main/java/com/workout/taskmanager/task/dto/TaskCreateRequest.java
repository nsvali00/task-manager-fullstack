package com.workout.taskmanager.task.dto;

public class TaskCreateRequest {

    private String name;
    private String description;
    private boolean completed;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}