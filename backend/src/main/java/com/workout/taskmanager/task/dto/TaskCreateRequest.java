package com.workout.taskmanager.task.dto;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.task.enums.TaskPriority;
import com.workout.taskmanager.user.entity.User;

import java.time.LocalDateTime;

public record TaskCreateRequest(

        String title,
        String description,
        TaskPriority priority,
        LocalDateTime dueDate,
        Long projectId,
        Long assigneeId
        ) {
}