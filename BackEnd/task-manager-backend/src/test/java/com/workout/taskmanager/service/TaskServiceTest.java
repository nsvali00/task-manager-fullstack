package com.workout.taskmanager.service;

import com.workout.taskmanager.task.dto.TaskCreateRequest;
import com.workout.taskmanager.task.dto.TaskUpdateRequest;
import com.workout.taskmanager.task.dto.TaskResponse;
import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.exceptions.TaskNotFoundException;
import com.workout.taskmanager.task.mapper.TaskMapper;
import com.workout.taskmanager.task.repository.TaskRepository;
import com.workout.taskmanager.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final Logger log =
            LoggerFactory.getLogger(TaskService.class);

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    // =========================
    // PHASE 2 — CREATE
    // =========================

    @Test
    void createTask_success() {


        TaskCreateRequest request = new TaskCreateRequest();
        request.setName("Gym");
        request.setDescription("Push day");
        request.setCompleted(false);

        Task task = new Task();
        Task savedTask = new Task();

        TaskResponse responseDto = new TaskResponse();
        responseDto.setName("Gym");
        responseDto.setDescription("Push day");
        responseDto.setCompleted(false);

        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(responseDto);

        log.info("Creating task with name {}", request.getName());
        TaskResponse result = taskService.createTask(request);

        assertEquals("Gym", result.getName());
        assertEquals("Push day", result.getDescription());

        verify(taskMapper).toEntity(request);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(savedTask);
        log.info("Task created with id: {}", savedTask.getId());


    }

    // =========================
    // PHASE 3 — READ
    // =========================

    @Test
    void getTaskById_success() {

        Long id = 1L;

        Task task = new Task();
        TaskResponse responseDto = new TaskResponse();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponse result = taskService.getTaskById(id);

        assertNotNull(result);
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

    // =========================
    // PHASE 4 — UPDATE
    // =========================

    @Test
    void updateTask_success() {

        Long id = 1L;

        TaskUpdateRequest request = new TaskUpdateRequest();

        Task task = new Task();
        TaskResponse responseDto = new TaskResponse();

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponse result = taskService.patchTask(id, request);

        assertNotNull(result);

        verify(taskRepository).findById(id);
        verify(taskMapper).updateTaskFromDto(request, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
    }

    @Test
    void updateTask_notFound() {
        Long id = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class,
                () -> taskService.patchTask(id, request));
    }

    // =========================
    // PHASE 5 — DELETE
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
}