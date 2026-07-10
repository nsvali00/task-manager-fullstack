package com.workout.taskmanager.sprint.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.common.dto.PageResponse;
import com.workout.taskmanager.project.dto.ProjectResponse;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.sprint.dto.SprintCreateRequest;
import com.workout.taskmanager.sprint.dto.SprintResponse;
import com.workout.taskmanager.sprint.dto.SprintUpdateRequest;
import com.workout.taskmanager.sprint.entity.Sprint;
import com.workout.taskmanager.sprint.service.SprintService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SprintController {

    private SprintService sprintService;

    @Operation(summary = "Create new sprint")
    @PostMapping("/projects/{projectId}/sprints")
    public ResponseEntity<ApiResponse<SprintResponse>> createSprint(@PathVariable Long projectId, @Valid @RequestBody SprintCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        SprintResponse sprint = sprintService.createSprint(projectId, request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(sprint, "Sprint created successfully"));
    }

    @Operation(summary = "Get all sprints of project with pagination, sorting")
    @GetMapping("/projects/{projectId}/sprints")
    public ResponseEntity<ApiResponse<PageResponse<SprintResponse>>> getSprintsForProject(Pageable pageable, @PathVariable Long projectId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<SprintResponse> allSprints = sprintService.getSprintsForProject(pageable, projectId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(allSprints), "Successfully got all sprints"));
    }

    @Operation(summary = "Get specific sprint by id")
    @GetMapping("/sprints/{sprintId}")
    public ResponseEntity<ApiResponse<SprintResponse>> getSprintById(@PathVariable Long sprintId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(sprintService.getSprintById(sprintId, userDetails.getUser()), "Succesfully got sprint with id " + sprintId));
    }

    @Operation(summary = "Update sprint ")
    @PatchMapping("/sprints/{sprintId}")
    public ResponseEntity<ApiResponse<SprintResponse>> updateSprint(@PathVariable Long sprintId, @RequestBody SprintUpdateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(sprintService.updateSprint(sprintId, request, userDetails.getUser()), "Successfully updated sprint with id " + sprintId));
    }

    @Operation(summary = "Delete sprint")
    @DeleteMapping("/sprints/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        sprintService.deleteSprint(id,userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
