package com.workout.taskmanager.sprint.dto;

import jakarta.validation.constraints.NotBlank;

public record SprintCreateRequest(@NotBlank String name, Long projectId) {
}
