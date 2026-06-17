package com.workout.taskmanager.project.repository;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<User, Long> {

    boolean existsByProjectAndUser(Project project, User user);
}
