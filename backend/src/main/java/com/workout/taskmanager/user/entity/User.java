package com.workout.taskmanager.user.entity;

import com.workout.taskmanager.common.enums.Role;
import com.workout.taskmanager.issue.entity.Issue;
import com.workout.taskmanager.project.entity.ProjectMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members;
    @OneToMany(mappedBy = "assignee")
    private List<Issue> assignedIssue = new ArrayList<>();
    @OneToMany(mappedBy = "reporter")
    private List<Issue> createdIssue = new ArrayList<>();

}
