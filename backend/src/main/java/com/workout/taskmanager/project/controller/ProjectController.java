package com.workout.taskmanager.project.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.common.dto.PageResponse;
import com.workout.taskmanager.project.dto.ProjectCreateRequest;
import com.workout.taskmanager.project.dto.ProjectResponse;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.service.ProjectService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@RequestBody ProjectCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Project project = projectService.createProject(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(ProjectResponse.from(project), "Project created successfully"));
    }

    @Operation(summary = "Get project by specific ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(ProjectResponse.from(project), "Found project with id " + id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProjectResponse>>> getAllProjects(Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<ProjectResponse> page = projectService.getAllProjects(pageable, userDetails.getUser()).map(ProjectResponse::from);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page), "Successfully fetched all projects"));
    }

}
