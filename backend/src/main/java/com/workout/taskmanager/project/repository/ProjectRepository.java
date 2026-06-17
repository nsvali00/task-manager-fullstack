package com.workout.taskmanager.project.repository;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
