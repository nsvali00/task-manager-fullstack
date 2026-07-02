package com.workout.taskmanager.task;

import com.workout.taskmanager.common.enums.Role;
import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
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
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectMemberRepository memberRepository;

    @InjectMocks
    private TaskService taskService;

    private User currentUser;
    private Task task;
    private TaskResponse responseDto;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        task = new Task();
        task.setCreatedBy(currentUser);
        task.setAssignee(currentUser);

        responseDto = new TaskResponse(
                1L, "Title", "Desc", TaskStatus.TODO, TaskPriority.LOW,
                null, 1L, 1L, 1L, LocalDateTime.now());
    }

    // =========================
    // CREATE
    // =========================

    @Test
    void createTask_success() {
        User assignee = new User();
        assignee.setId(2L);
        Project project = new Project();

        TaskCreateRequest request = new TaskCreateRequest(
                "Fix bug", "Fix the login bug", TaskPriority.HIGH, LocalDateTime.now().plusDays(1), 1L, 2L);

        Task newTask = new Task();
        Task savedTask = new Task();
        TaskResponse createResponse = new TaskResponse(
                1L, "Fix bug", "Fix the login bug", TaskStatus.TODO, TaskPriority.HIGH,
                null, 2L, 1L, 1L, LocalDateTime.now());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberRepository.existsByProjectAndUser(project, currentUser)).thenReturn(true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(memberRepository.existsByProjectAndUser(project, assignee)).thenReturn(true);
        when(taskMapper.toEntity(request)).thenReturn(newTask);
        when(taskRepository.save(newTask)).thenReturn(savedTask);
        when(taskMapper.toDto(savedTask)).thenReturn(createResponse);

        TaskResponse result = taskService.createTask(request, currentUser);

        assertEquals("Fix bug", result.getTitle());
        verify(taskRepository).save(newTask);
    }

    @Test
    void createTask_notProjectMember_throws() {
        Project project = new Project();
        TaskCreateRequest request = new TaskCreateRequest(
                "Fix bug", "Desc", TaskPriority.HIGH, null, 1L, null);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberRepository.existsByProjectAndUser(project, currentUser)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.createTask(request, currentUser));
    }

    // =========================
    // READ
    // =========================

    @Test
    void getTaskById_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        TaskResponse result = taskService.getTaskById(1L, currentUser);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void getTaskById_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L, currentUser));
    }

    @Test
    void getTaskById_accessDenied() {
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setRole(Role.USER);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.getTaskById(1L, otherUser));
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void patchTask_success() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated title");

        TaskResponse updateResponse = new TaskResponse(
                1L, "Updated title", "Desc", TaskStatus.TODO, TaskPriority.LOW,
                null, 1L, 1L, 1L, LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(updateResponse);

        TaskResponse result = taskService.patchTask(1L, request, currentUser);

        assertEquals("Updated title", result.getTitle());
        verify(taskMapper).updateTaskFromDto(request, task);
    }

    @Test
    void patchTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.patchTask(1L, new TaskUpdateRequest(), currentUser));
    }

    @Test
    void patchTask_accessDenied() {
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setRole(Role.USER);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.patchTask(1L, new TaskUpdateRequest(), otherUser));
    }

    // =========================
    // DELETE
    // =========================

    @Test
    void deleteTask_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, currentUser);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L, currentUser));
    }

    @Test
    void deleteTask_accessDenied() {
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setRole(Role.USER);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.deleteTask(1L, otherUser));
    }

    // =========================
    // SEARCH
    // =========================

    @Test
    void searchTasks_success() {
        Pageable pageable = PageRequest.of(0, 10);
        CustomUserDetails userDetails = new CustomUserDetails(currentUser);

        when(taskRepository.findByUserAndTitleContainingIgnoreCase(currentUser, "Fix", pageable))
                .thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toDto(task)).thenReturn(responseDto);

        List<TaskResponse> result = taskService.searchTasks("Fix", pageable, userDetails);

        assertEquals(1, result.size());
    }
}
