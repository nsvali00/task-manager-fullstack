package com.workout.taskmanager.project.dto;

import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.enums.ProjectRole;

import java.time.LocalDateTime;

public record ProjectMemberResponse(Long id, Long projectId, String projectName, Long userId,
                                    String userEmail, String userFirstName, String userLastName,
                                    ProjectRole role, LocalDateTime joinedAt) {

    public static ProjectMemberResponse from(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getProject().getId(),
                member.getProject().getName(),
                member.getUser().getId(),
                member.getUser().getEmail(),
                member.getUser().getFirstName(),
                member.getUser().getLastName(),
                member.getRole(),
                member.getJoinedAt()
        );
    }
}
