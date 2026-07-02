package com.workout.taskmanager.project.dto;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerEmail;
    private String ownerFirstName;
    private String ownerLastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MemberSummary> members;

    @Getter
    @AllArgsConstructor
    public static class MemberSummary {
        private Long id;
        private Long userId;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private LocalDateTime joinedAt;
    }

    public static ProjectResponse from(Project project) {
        List<MemberSummary> memberSummaries = null;
        if (project.getMembers() != null) {
            memberSummaries = project.getMembers().stream()
                    .map(m -> new MemberSummary(
                            m.getId(),
                            m.getUser().getId(),
                            m.getUser().getEmail(),
                            m.getUser().getFirstName(),
                            m.getUser().getLastName(),
                            m.getRole() != null ? m.getRole().name() : null,
                            m.getJoinedAt()
                    ))
                    .toList();
        }

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getOwner().getEmail(),
                project.getOwner().getFirstName(),
                project.getOwner().getLastName(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                memberSummaries
        );
    }
}
