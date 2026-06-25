package com.workout.taskmanager.task.repository;

import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserAndTitleContainingIgnoreCase(
            User user,
            String title,
            Pageable pageable
    );


    List<Task> findByUserAndProjectId(User user, Long projectId);
    Page<Task> findByUser(User user, Pageable pageable);
}
