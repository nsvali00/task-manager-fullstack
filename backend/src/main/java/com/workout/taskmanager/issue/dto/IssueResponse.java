package com.workout.taskmanager.issue.dto;

import com.workout.taskmanager.issue.entity.Issue;
import com.workout.taskmanager.issue.enums.IssuePriority;
import com.workout.taskmanager.issue.enums.IssueStatus;
import com.workout.taskmanager.issue.enums.IssueType;

public record IssueResponse(Long id,String title, String description, IssueStatus status, IssuePriority priority, IssueType type,
                            Long projectId, Long sprintId, Long assigneeId, Long reporterId) {

    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getType(),
                issue.getProject().getId(),
                issue.getSprint().getId(),
                issue.getAssignee() != null ? issue.getAssignee().getId() : null,
                issue.getReporter().getId()
        );
    }
}
