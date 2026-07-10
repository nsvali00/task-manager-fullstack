package com.workout.taskmanager.issue.dto;

import com.workout.taskmanager.issue.enums.IssuePriority;
import com.workout.taskmanager.issue.enums.IssueType;

public record IssueCreateRequest (String title, String description, IssuePriority priority, IssueType type, Long assigneeId){ }
