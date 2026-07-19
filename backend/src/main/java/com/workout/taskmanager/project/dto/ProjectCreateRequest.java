package com.workout.taskmanager.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank(message = "Project name must not be blank")
        @Size(max = 100, message = "Project name must not exceed 100 characters")
        String name,

        @Size(max = 500, message = "Project description must not exceed 500 characters")
        String description
) {}
