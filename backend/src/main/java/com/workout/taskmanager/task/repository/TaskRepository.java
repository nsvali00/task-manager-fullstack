package com.workout.taskmanager.task.repository;

import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE (t.assignee = :user OR t.createdBy = :user) AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Task> findByUserAndTitleContainingIgnoreCase(@Param("user") User user, @Param("title") String title, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.assignee = :user OR t.createdBy = :user")
    Page<Task> findByUser(@Param("user") User user, Pageable pageable);
}
