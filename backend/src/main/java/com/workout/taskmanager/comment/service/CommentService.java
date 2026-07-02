package com.workout.taskmanager.comment.service;

import com.workout.taskmanager.comment.dto.CommentCreateRequest;
import com.workout.taskmanager.comment.dto.CommentResponse;
import com.workout.taskmanager.comment.entity.Comment;
import com.workout.taskmanager.comment.repository.CommentRepository;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.exceptions.TaskNotFoundException;
import com.workout.taskmanager.task.repository.TaskRepository;
import com.workout.taskmanager.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentResponse addComment(Long taskId, CommentCreateRequest request, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        comment = commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    public Page<CommentResponse> getCommentsByTask(Long taskId, Pageable pageable) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        return commentRepository.findByTaskId(taskId, pageable)
                .map(CommentResponse::from);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}
