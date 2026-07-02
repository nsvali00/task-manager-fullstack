package com.workout.taskmanager.task.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.task.dto.TaskCreateRequest;
import com.workout.taskmanager.task.dto.TaskUpdateRequest;
import com.workout.taskmanager.task.dto.TaskResponse;
import com.workout.taskmanager.task.service.TaskService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task Controller", description = "Task management APIs")
@Valid
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks with pagination, sorting")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getAllTask(Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<TaskResponse> allTasks = taskService.getAllTasks(pageable, userDetails.getUser());
        ApiResponse<Page<TaskResponse>> apiResponse = ApiResponse.success(allTasks, "Tasks fetched successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get task by specific ID")
    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskResponse taskResponseDTO = taskService.getTaskById(id, userDetails.getUser());
        ApiResponse<TaskResponse> apiResponse = ApiResponse.success(taskResponseDTO,"Task fetched successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Create new task")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody TaskCreateRequest newTask,
//                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
                                                                Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaskResponse created = taskService.createTask(newTask, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created, "Task created successfully"));
    }

    @Operation(summary = "Update task")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> patch(@PathVariable Long id, @RequestBody TaskUpdateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskResponse updated = taskService.patchTask(id, request, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(updated, "Task updated"));
    }

    @Operation(summary = "Remove specific task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.deleteTask(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search tasks by name with pagination and sorting")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> searchTasks(
            @RequestParam String name, @ParameterObject Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TaskResponse> result = taskService.searchTasks(name, pageable, userDetails);
        return ResponseEntity.ok(ApiResponse.success(result, "Search results")
        );
    }


}
