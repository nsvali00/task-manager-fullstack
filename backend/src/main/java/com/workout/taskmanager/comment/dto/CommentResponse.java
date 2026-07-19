package com.workout.taskmanager.comment.dto;

import com.workout.taskmanager.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, Long issueId, Long userId, String userEmail,
                              String userFirstName, String userLastName, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getIssue().getId(),
                comment.getUser().getId(),
                comment.getUser().getEmail(),
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getCreatedAt()
        );
    }
}
