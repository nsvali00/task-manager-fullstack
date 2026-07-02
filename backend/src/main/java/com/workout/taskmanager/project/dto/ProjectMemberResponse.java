package com.workout.taskmanager.project.dto;

import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.enums.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectMemberResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private ProjectRole role;
    private LocalDateTime joinedAt;

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
