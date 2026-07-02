package com.workout.taskmanager.comment;

import com.workout.taskmanager.comment.dto.CommentCreateRequest;
import com.workout.taskmanager.comment.dto.CommentResponse;
import com.workout.taskmanager.comment.entity.Comment;
import com.workout.taskmanager.comment.repository.CommentRepository;
import com.workout.taskmanager.comment.service.CommentService;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.exceptions.TaskNotFoundException;
import com.workout.taskmanager.task.repository.TaskRepository;
import com.workout.taskmanager.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setTask(task);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void addComment_success() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("New comment");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse result = commentService.addComment(1L, request, user);

        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        assertEquals(1L, result.getTaskId());
        assertEquals(1L, result.getUserId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_taskNotFound() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("New comment");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.addComment(1L, request, user));
    }

    @Test
    void getCommentsByTask_success() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findByTaskId(1L, pageable))
                .thenReturn(new PageImpl<>(List.of(comment)));

        Page<CommentResponse> result = commentService.getCommentsByTask(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test comment", result.getContent().get(0).getContent());
    }

    @Test
    void getCommentsByTask_taskNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> commentService.getCommentsByTask(1L, pageable));
    }

    @Test
    void deleteComment_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, user);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_notFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(1L, user));
    }

    @Test
    void deleteComment_notOwner() {
        User otherUser = new User();
        otherUser.setId(99L);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertThrows(AccessDeniedException.class, () -> commentService.deleteComment(1L, otherUser));
    }
}
