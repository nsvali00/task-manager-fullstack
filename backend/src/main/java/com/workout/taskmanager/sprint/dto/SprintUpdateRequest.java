package com.workout.taskmanager.sprint.dto;

import com.workout.taskmanager.sprint.enums.SprintStatus;
import java.time.LocalDateTime;

public record SprintUpdateRequest(String name, SprintStatus status) {}
