package com.workout.taskmanager.project.entity;

import com.workout.taskmanager.project.enums.ProjectRole;
import com.workout.taskmanager.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_project_member_project_user",
                columnNames = {"project_id", "user_id"})
        }
)
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    private LocalDateTime joinedAt;
    private ProjectRole role;
}
