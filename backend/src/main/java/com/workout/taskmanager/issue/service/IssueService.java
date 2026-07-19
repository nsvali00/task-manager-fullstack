package com.workout.taskmanager.issue.service;

import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.issue.dto.IssueCreateRequest;
import com.workout.taskmanager.issue.dto.IssueResponse;
import com.workout.taskmanager.issue.dto.IssueUpdateRequest;
import com.workout.taskmanager.issue.entity.Issue;
import com.workout.taskmanager.issue.enums.IssueStatus;
import com.workout.taskmanager.issue.repository.IssueRepository;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.sprint.entity.Sprint;
import com.workout.taskmanager.sprint.repository.SprintRepository;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class IssueService {

    private IssueRepository issueRepository;
    private ProjectRepository projectRepository;
    private ProjectMemberRepository memberRepository;
    private UserRepository userRepository;
    private SprintRepository sprintRepository;

    // Create issue
    @Transactional
    public IssueResponse createIssue(IssueCreateRequest request, Long projectId, User user){
        Project project = checkMembershipAndGetProject(projectId,user);
        Long assigneeId = request.assigneeId();
        Issue issue = new Issue();
        if(assigneeId!= null){
            User assignee = userRepository.findById(assigneeId).orElseThrow(() -> new UsernameNotFoundException("Failed to find user with id " + assigneeId));
            checkMembership(project,assignee);
            issue.setAssignee(assignee);
        }

        issue.setDescription(request.description());
        issue.setPriority(request.priority());
        issue.setTitle(request.title());
        issue.setType(request.type());
        issue.setStatus(IssueStatus.TODO);
        issue.setReporter(user);
        issue.setSprint(null);
        issue.setProject(project);
        issue = issueRepository.save(issue);
        return IssueResponse.from(issue);
    }

    //Get issue by id
    @Transactional(readOnly = true)
    public IssueResponse  getIssueById(Long issueId, User user){
        Issue wantedIssue = getIssueOrThrow(issueId);
        checkMembershipAndGetProject(wantedIssue.getProject().getId(), user);
        return IssueResponse.from(wantedIssue);
    }

    //Update issue
    @Transactional
    public IssueResponse updateIssue(Long issueId, IssueUpdateRequest request, User user){
        Issue issue = getIssueOrThrow(issueId);
        checkMembershipAndGetProject(issue.getProject().getId(), user);
        if (request.title() != null) {
            issue.setTitle(request.title());
        }
        if (request.description() != null) {
            issue.setDescription(request.description());
        }
        if (request.status() != null) {
            issue.setStatus(request.status());
        }
        if (request.priority() != null) {
            issue.setPriority(request.priority());
        }
        if (request.type() != null) {
            issue.setType(request.type());
        }

        issue.setAssignee(resolveAssignee(request.assigneeId(), issue.getProject().getId()));
        issue.setSprint(resolveSprint(request.sprintId(), issue.getProject().getId()));

        issueRepository.save(issue);
        return IssueResponse.from(issue);
    }
    //delete issue
    @Transactional
    public void deleteIssueById(Long issueId, User user){
        Issue issue = getIssueOrThrow(issueId);
        checkMembershipAndGetProject(issue.getProject().getId(), user);
        issueRepository.delete(issue);
    }

    // list issue for project
    @Transactional(readOnly = true)
    public Page<IssueResponse> getAllIssuesForProject(Long projectId, Pageable pageable, User user){
        checkMembershipAndGetProject(projectId,user);
        Page<Issue> issues = issueRepository.findByProjectId(projectId, pageable);
        return issues.map(IssueResponse::from);
    }

    private @NonNull Project checkMembershipAndGetProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        checkMembership(project,currentUser);
        return project;
    }

    private void checkMembership(Project project, User user){
        if (!memberRepository.existsByProjectAndUser(project, user)) {
            throw new AccessDeniedException("Not a project member");
        }
    }

    private @NonNull Issue getIssueOrThrow(Long issueId) {
        return issueRepository.findById(issueId).orElseThrow(() -> new ResourceNotFoundException("Failed to find issue with id: " + issueId));
    }

    private User resolveAssignee(Long assigneeId, Long projectId) {
        if (assigneeId == null) return null;

        // must be a member of the project to be assignable
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Failed to find project with id " + projectId));
        User assignee = userRepository.findById(assigneeId).orElseThrow(() -> new ResourceNotFoundException("Failed to find user with id " + assigneeId));
        ProjectMember membership = memberRepository.findByProjectAndUser(project, assignee)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User " + assigneeId + " is not a member of project " + projectId + " and cannot be assigned"));

        return membership.getUser();
    }

    private Sprint resolveSprint(Long sprintId, Long projectId) {
        if (sprintId == null) return null; // moving back to backlog

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id " + sprintId));

        if (!sprint.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Sprint does not belong to this issue's project");
        }

        return sprint;
    }
}
