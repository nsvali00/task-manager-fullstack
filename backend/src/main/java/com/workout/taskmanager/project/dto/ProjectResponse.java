package com.workout.taskmanager.project.dto;

import com.workout.taskmanager.project.entity.Project;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(Long id, String name, String description, Long ownerId, String ownerEmail,
                              String ownerFirstName, String ownerLastName, LocalDateTime createdAt,
                              LocalDateTime updatedAt, List<MemberSummary> members) {

    public record MemberSummary(Long id, Long userId, String email, String firstName,
                                String lastName, String role, LocalDateTime joinedAt) {
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
