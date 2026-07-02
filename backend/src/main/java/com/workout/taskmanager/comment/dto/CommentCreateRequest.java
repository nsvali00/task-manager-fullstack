package com.workout.taskmanager.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    @NotBlank(message = "Comment content must not be blank")
    private String content;
}
