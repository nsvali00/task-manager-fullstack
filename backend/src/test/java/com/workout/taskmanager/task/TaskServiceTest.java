package com.workout.taskmanager.task;

import com.workout.taskmanager.task.dto.TaskCreateRequest;
import com.workout.taskmanager.task.dto.TaskUpdateRequest;
import com.workout.taskmanager.task.dto.TaskResponse;
import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.enums.TaskPriority;
import com.workout.taskmanager.task.enums.TaskStatus;
import com.workout.taskmanager.task.exceptions.TaskNotFoundException;
import com.workout.taskmanager.task.mapper.TaskMapper;
import com.workout.taskmanager.task.repository.TaskRepository;
import com.workout.taskmanager.task.service.TaskService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    // =========================
    // CREATE
    // =========================

    @Test
    void createTask_success() {
        TaskCreateRequest request = new TaskCreateRequest(
                "Fix bug", "Fix the login bug", TaskPriority.HIGH, LocalDateTime.now().plusDays(1), null, 2L);

        Task task = new Task();
        Task savedTask = new Task();

        TaskResponse responseDto = new TaskResponse(
                1L, "Fix bug", "Fix the login bug", TaskStatus.TODO, TaskPriority.HIGH,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(responseDto);

        TaskResponse result = taskService.createTask(request);

        assertEquals("Fix bug", result.getTitle());
        assertEquals("Fix the login bug", result.getDescription());

        verify(taskMapper).toEntity(request);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(savedTask);
    }

    // =========================
    // READ
    // =========================

    @Test
    void getTaskById_success() {
        Long id = 1L;
        Task task = new Task();
        TaskResponse responseDto = new TaskResponse(
                1L, "Title", "Desc", TaskStatus.TODO, TaskPriority.LOW,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponse result = taskService.getTaskById(id);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(taskRepository).findById(id);
        verify(taskMapper).toDto(task);
    }

    @Test
    void getTaskById_notFound() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.getTaskById(id));
    }

    @Test
    void getAllTasks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        Page<Task> taskPage = new PageImpl<>(List.of(task));
        TaskResponse responseDto = new TaskResponse(
                1L, "Title", "Desc", TaskStatus.TODO, TaskPriority.LOW,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        Page<TaskResponse> result = taskService.getAllTasks(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Title", result.getContent().get(0).getTitle());
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void patchTask_success() {
        Long id = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated title");

        Task task = new Task();
        TaskResponse responseDto = new TaskResponse(
                1L, "Updated title", "Desc", TaskStatus.TODO, TaskPriority.LOW,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponse result = taskService.patchTask(id, request);

        assertNotNull(result);
        assertEquals("Updated title", result.getTitle());

        verify(taskRepository).findById(id);
        verify(taskMapper).updateTaskFromDto(request, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
    }

    @Test
    void patchTask_notFound() {
        Long id = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.patchTask(id, request));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void deleteTask_success() {
        Long id = 1L;
        Task task = new Task();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.deleteTask(id);

        verify(taskRepository).findById(id);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_notFound() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteTask(id));
    }

    // =========================
    // SEARCH
    // =========================

    @Test
    void searchTasks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        TaskResponse responseDto = new TaskResponse(
                1L, "Fix bug", "Desc", TaskStatus.TODO, TaskPriority.HIGH,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(taskRepository.findByNameContainingIgnoreCase("Fix", pageable)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        List<TaskResponse> result = taskService.searchTasks("Fix", pageable);

        assertEquals(1, result.size());
        assertEquals("Fix bug", result.get(0).getTitle());
    }
}
