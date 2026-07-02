package com.workout.taskmanager.user.entity;

import com.workout.taskmanager.common.enums.Role;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.task.entity.Task;
import jakarta.persistence.*;
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
    private List<Task> assignedTasks = new ArrayList<>();
    @OneToMany(mappedBy = "createdBy")
    private List<Task> createdTasks = new ArrayList<>();

}
