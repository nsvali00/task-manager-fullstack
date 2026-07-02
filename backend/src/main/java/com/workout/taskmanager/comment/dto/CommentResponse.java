package com.workout.taskmanager.comment.dto;

import com.workout.taskmanager.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long taskId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getUser().getEmail(),
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getCreatedAt()
        );
    }
}
