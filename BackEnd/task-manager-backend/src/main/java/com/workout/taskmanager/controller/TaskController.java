package com.workout.taskmanager.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.dto.request.TaskCreateRequest;
import com.workout.taskmanager.dto.request.TaskUpdateRequest;
import com.workout.taskmanager.dto.response.TaskResponseDTO;
import com.workout.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task Controller", description = "Task management APIs")
@Valid
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks with pagination, sorting")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getAllTask(Pageable pageable){
        Page<TaskResponseDTO> allTasks = taskService.getAllTasks(pageable);
        ApiResponse<Page<TaskResponseDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(allTasks);
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Tasks fetched successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Get task by specific ID")
    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Long id){
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(id);
        ApiResponse<TaskResponseDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(taskResponseDTO);
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Task fetched successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Create new task")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(@RequestBody TaskCreateRequest newTask){
        TaskResponseDTO createdTaskDTO = taskService.createTask(newTask);
        ApiResponse<TaskResponseDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(createdTaskDTO);
        apiResponse.setStatus(HttpStatus.CREATED);
        apiResponse.setMessage("Task created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @Operation(summary = "Update task")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> patch(
            @PathVariable Long id,
            @RequestBody TaskUpdateRequest request) {

        TaskResponseDTO updated = taskService.patchTask(id, request);

        return ResponseEntity.ok(new ApiResponse<>(updated, "Task updated", HttpStatus.OK));
    }

    @Operation(summary = "Remove specific task by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search tasks by name with pagination and sorting")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> searchTasks(
            @RequestParam String name, @ParameterObject Pageable pageable) {
        List<TaskResponseDTO> result = taskService.searchTasks(name, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(result, "Search results", HttpStatus.OK)
        );
    }





}
