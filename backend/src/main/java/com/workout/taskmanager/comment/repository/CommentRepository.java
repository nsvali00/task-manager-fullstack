package com.workout.taskmanager.comment.repository;

import com.workout.taskmanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<User, Long> {
}
