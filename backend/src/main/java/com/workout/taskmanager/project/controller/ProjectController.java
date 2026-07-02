package com.workout.taskmanager.project.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.project.dto.ProjectCreateRequest;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.service.ProjectService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {
    ProjectService projectService;

    @Operation(summary = "Create new project")
    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody ProjectCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(projectService.createProject(request, userDetails.getUser()), "Project created successfully"));
    }
    @Operation(summary = "Get project by specific ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectById(id), "Found project with id " + id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Project>>> getAllProjects(Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects(pageable), "Successfully fetched all projects"));
    }

}
