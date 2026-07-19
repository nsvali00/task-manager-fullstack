package com.workout.taskmanager.comment.controller;

import com.workout.taskmanager.comment.dto.CommentCreateRequest;
import com.workout.taskmanager.comment.dto.CommentResponse;
import com.workout.taskmanager.comment.service.CommentService;
import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.common.dto.PageResponse;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues/{issueId}/comments")
@AllArgsConstructor
@Tag(name = "Comment Controller", description = "Issue comment APIs")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a comment to a issue")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long issueId,
            @Valid @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        CommentResponse comment = commentService.addComment(issueId, request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(comment, "Comment added successfully"));
    }

    @Operation(summary = "Get all comments for an issue")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(
            @PathVariable Long issueId,
            Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<CommentResponse> comments = commentService.getCommentsByIssue(issueId, pageable, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(comments), "Comments retrieved successfully"));
    }

    @Operation(summary = "Delete a comment")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, issueId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
