package com.workout.taskmanager.task.service;

import com.workout.taskmanager.common.enums.Role;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
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
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository memberRepository;


    public Page<TaskResponse> getAllTasks(Pageable pageable, User user) {
        Page<Task> allTasks = taskRepository.findByUser(user,pageable);
        return allTasks.map(TaskResponse::from);
    }

    public TaskResponse getTaskById(Long id, User user) {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Task not found with id {}", id);
            return new TaskNotFoundException(id);
        });
        checkAccess(task,user);
        return TaskResponse.from(task);
    }

    public TaskResponse createTask(TaskCreateRequest newTaskDto, User currentUser) {
        log.info("Creating task with title: {}", newTaskDto.title());
        Project project = projectRepository.findById(newTaskDto.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + newTaskDto.projectId()));
        if (!memberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new AccessDeniedException("Not a project member");
        }
        User assignee = null;
        if(newTaskDto.assigneeId() != null){
            assignee = userRepository.findById(newTaskDto.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + newTaskDto.assigneeId()));
        }
        if (assignee != null &&
                !memberRepository.existsByProjectAndUser(project, assignee)) {
            throw new AccessDeniedException("Assignee must be a project member");
        }
        Task task = new Task();
        task.setTitle(newTaskDto.title());
        task.setDescription(newTaskDto.description());
        task.setPriority(newTaskDto.priority());
        task.setDueDate(newTaskDto.dueDate());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignee(assignee);
        task.setCreatedBy(currentUser);
        task = taskRepository.save(task);
        log.info("Task created successfully with id: {}", task.getId());
        return TaskResponse.from(task);
    }

    public TaskResponse patchTask(Long id, TaskUpdateRequest request, User user) {
        log.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update, task not found with id: {}", id);
                    return new TaskNotFoundException(id);
                });
        checkAccess(task,user);
        taskMapper.updateTaskFromDto(request, task);
        task = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", id);
        return TaskResponse.from(task);
    }

    public void deleteTask(Long id, User user) {
        log.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            log.warn("Cannot delete, task not found with id: {}", id);
            return new TaskNotFoundException(id);
        });
        checkAccess(task,user);
        taskRepository.delete(task);
    }

    public List<TaskResponse> searchTasks(String title, Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails){
        Page<Task> tasks = taskRepository.findByUserAndTitleContainingIgnoreCase(userDetails.getUser(), title, pageable);
        return tasks.stream().map(TaskResponse::from).toList();
    }

    private void checkAccess(Task task, User user){
        boolean isOwner = task.getCreatedBy().getId().equals(user.getId()) || task.getAssignee().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("Access denied");
        }
    }



}
