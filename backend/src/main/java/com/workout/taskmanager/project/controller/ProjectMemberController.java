package com.workout.taskmanager.project.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.project.dto.ProjectMemberResponse;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.enums.ProjectRole;
import com.workout.taskmanager.project.service.ProjectMemberService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project-members")
@AllArgsConstructor
public class ProjectMemberController {

    ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectMemberResponse>> addMember(@RequestParam Long projectId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long userId, @RequestParam ProjectRole role){
        ProjectMember member = projectMemberService.addMember(projectId, userDetails.getUser(), userId, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(ProjectMemberResponse.from(member), "Successfully created new project member"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeMember(@RequestParam Long projectId, @AuthenticationPrincipal CustomUserDetails userDetails, Long userId){
        projectMemberService.removeMember(projectId,userDetails.getUser());
        return ResponseEntity.noContent().build();
    }


}
