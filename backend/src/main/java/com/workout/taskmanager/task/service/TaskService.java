package com.workout.taskmanager.task.service;

import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.task.dto.TaskCreateRequest;
import com.workout.taskmanager.task.dto.TaskUpdateRequest;
import com.workout.taskmanager.task.dto.TaskResponse;
import com.workout.taskmanager.task.enums.TaskStatus;
import com.workout.taskmanager.task.exceptions.TaskNotFoundException;
import com.workout.taskmanager.task.mapper.TaskMapper;
import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.repository.TaskRepository;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
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
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository memberRepository;


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository, UserRepository userRepository, ProjectMemberRepository memberRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        Page<Task> allTasks = taskRepository.findAll(pageable);
        return allTasks.map(taskMapper::toDto);
    }

    public TaskResponse getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Task not found with id {}", id);
            return new TaskNotFoundException(id);
        });
        return taskMapper.toDto(task);
    }

    public TaskResponse createTask(TaskCreateRequest newTaskDto, User currentUser) {
        log.info("Creating task with title: {}", newTaskDto.title());
        Project project = projectRepository.findById(newTaskDto.projectId()).orElseThrow(()->new RuntimeException("Failed to find project with id " + newTaskDto.projectId()));
        if (memberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new RuntimeException("Not a project member");
        }
        User assignee = null;
        if(newTaskDto.assigneeId() != null){
            assignee = userRepository.findById(newTaskDto.assigneeId()).orElseThrow(()-> new RuntimeException("Failed to find assignee by id " + newTaskDto.assigneeId()));
        }
        if (assignee != null &&
                memberRepository.existsByProjectAndUser(project, assignee)) {
            throw new RuntimeException("Assignee must be project member");
        }

        Task task = taskMapper.toEntity(newTaskDto);
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignee(assignee);
        task.setCreatedBy(currentUser);
        task = taskRepository.save(task);
        log.info("Task created successfully with id: {}", task.getId());
        return taskMapper.toDto(task);
    }

    public TaskResponse patchTask(Long id, TaskUpdateRequest request) {
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

    public List<TaskResponse> searchTasks(String name, Pageable pageable){
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name, pageable);

        return tasks.stream().map(taskMapper::toDto).toList();
    }


}
