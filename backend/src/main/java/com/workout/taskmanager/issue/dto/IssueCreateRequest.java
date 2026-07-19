package com.workout.taskmanager.issue.dto;

import com.workout.taskmanager.issue.enums.IssuePriority;
import com.workout.taskmanager.issue.enums.IssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IssueCreateRequest(
        @NotBlank(message = "Issue title must not be blank")
        @Size(max = 200, message = "Issue title must not exceed 200 characters")
        String title,

        @Size(max = 2000, message = "Issue description must not exceed 2000 characters")
        String description,

        IssuePriority priority,
        IssueType type,
        Long assigneeId
) {}
