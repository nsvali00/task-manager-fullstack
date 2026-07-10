package com.workout.taskmanager.issue.entity;

import com.workout.taskmanager.issue.enums.IssuePriority;
import com.workout.taskmanager.issue.enums.IssueStatus;
import com.workout.taskmanager.issue.enums.IssueType;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.sprint.entity.Sprint;
import com.workout.taskmanager.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private IssueType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    public Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    public User assignee;
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    public User reporter;



}
