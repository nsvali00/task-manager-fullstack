package com.workout.taskmanager.task.dto;

import com.workout.taskmanager.task.enums.TaskPriority;
import com.workout.taskmanager.task.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TaskUpdateRequest {

    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private Long assigneeId;


}
