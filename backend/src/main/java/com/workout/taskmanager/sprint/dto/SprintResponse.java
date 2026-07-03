package com.workout.taskmanager.sprint.dto;

import com.workout.taskmanager.sprint.entity.Sprint;
import com.workout.taskmanager.sprint.enums.SprintStatus;

import java.time.LocalDateTime;


public record SprintResponse(Long id, String name, SprintStatus status, Long projectId, LocalDateTime createdAt) {
    public static SprintResponse from(Sprint sprint) {
        return new SprintResponse(
                sprint.getId(),
                sprint.getName(),
                sprint.getStatus(),
                sprint.getProject().getId(),
                sprint.getCreatedAt()
        );
    }

}
