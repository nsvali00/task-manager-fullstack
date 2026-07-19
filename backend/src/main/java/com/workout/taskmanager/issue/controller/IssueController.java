package com.workout.taskmanager.issue.controller;

import com.workout.taskmanager.common.ApiResponse;
import com.workout.taskmanager.common.dto.PageResponse;
import com.workout.taskmanager.issue.dto.IssueCreateRequest;
import com.workout.taskmanager.issue.dto.IssueResponse;
import com.workout.taskmanager.issue.dto.IssueUpdateRequest;
import com.workout.taskmanager.issue.service.IssueService;
import com.workout.taskmanager.user.entity.CustomUserDetails;
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
@RequestMapping("/api")
@AllArgsConstructor
public class IssueController {
    private IssueService issueService;

    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<ApiResponse<IssueResponse>> createIssue(@RequestBody @Valid IssueCreateRequest request, @PathVariable Long projectId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        IssueResponse createdIssue = issueService.createIssue(request, projectId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(createdIssue, "Successfully created issue"));
    }

    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<ApiResponse<PageResponse<IssueResponse>>> getAllIssuesForProject(@PathVariable Long projectId, Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<IssueResponse> allIssuesForProject = issueService.getAllIssuesForProject(projectId, pageable, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(allIssuesForProject), "Successfully got all issues for project with id " + projectId));
    }

    @GetMapping("/issues/{issueId}")
    public ResponseEntity<ApiResponse<IssueResponse>> getIssueById(@PathVariable Long issueId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        IssueResponse issue = issueService.getIssueById(issueId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(issue, "Successfully got issue with id " + issueId));
    }

    @PatchMapping("/issues/{issueId}")
    public ResponseEntity<ApiResponse<IssueResponse>> updateIssue(@PathVariable Long issueId, @RequestBody @Valid IssueUpdateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        IssueResponse updatedIssue = issueService.updateIssue(issueId, request, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(updatedIssue, "Successfully updated issue with id " + issueId));
    }

    @DeleteMapping("/issues/{issueId}")
    public ResponseEntity<ApiResponse<Void>> deleteIssue(@PathVariable Long issueId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        issueService.deleteIssueById(issueId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null, "Issue deleted successfully"));

    }
}
