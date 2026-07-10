package com.workout.taskmanager.issue.dto;

import com.workout.taskmanager.issue.enums.IssuePriority;
import com.workout.taskmanager.issue.enums.IssueStatus;
import com.workout.taskmanager.issue.enums.IssueType;

public record IssueUpdateRequest(String title, String description, IssueStatus status, IssuePriority priority, IssueType type, Long assigneeId, Long sprintId) {
}
