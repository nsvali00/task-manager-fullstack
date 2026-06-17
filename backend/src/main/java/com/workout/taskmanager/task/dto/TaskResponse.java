package com.workout.taskmanager.task.dto;

import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.enums.TaskPriority;
import com.workout.taskmanager.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private Long assigneeId;
    private Long createdById;
    private Long projectId;
    private LocalDateTime createdAt;

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getCreatedBy().getId(),
                task.getProject().getId(),
                task.getCreatedAt()
        );
    }
}

