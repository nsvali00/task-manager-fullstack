package com.workout.taskmanager.project.repository;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.members m WHERE p.owner = :user OR m.user = :user")
    Page<Project> findByUser(User user, Pageable pageable);
}
