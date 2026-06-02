package com.workout.taskmanager.service;

import com.workout.taskmanager.dto.request.TaskCreateRequest;
import com.workout.taskmanager.dto.request.TaskUpdateRequest;
import com.workout.taskmanager.dto.response.TaskResponseDTO;
import com.workout.taskmanager.exceptions.TaskNotFoundException;
import com.workout.taskmanager.mapper.TaskMapper;
import com.workout.taskmanager.entity.Task;
import com.workout.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        Page<Task> allTasks = taskRepository.findAll(pageable);
        return allTasks.map(taskMapper::toDto);
    }

    public TaskResponseDTO getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Task not found with id {}", id);
            return new TaskNotFoundException(id);
        });
        return taskMapper.toDto(task);
    }

    public TaskResponseDTO createTask(TaskCreateRequest newTaskDto) {
        log.info("Creating task with name: {}", newTaskDto.getName());
        Task task = taskMapper.toEntity(newTaskDto);
        task = taskRepository.save(task);
        log.info("Task created successfully with id: {}", task.getId());
        return taskMapper.toDto(task);
    }

    public TaskResponseDTO patchTask(Long id, TaskUpdateRequest request) {
        log.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update, task not found with id: {}", id);
                    return new TaskNotFoundException(id);
                });
        taskMapper.updateTaskFromDto(request, task);
        task = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", id);
        return taskMapper.toDto(task);
    }

    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Cannot delete, task not found with id: {}", id);
            return new TaskNotFoundException(id);
        });
        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> searchTasks(String name, Pageable pageable){
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name, pageable);

        return tasks.stream().map(taskMapper::toDto).toList();
    }


}
