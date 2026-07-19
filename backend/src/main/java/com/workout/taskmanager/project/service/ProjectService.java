package com.workout.taskmanager.project.service;

import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.project.dto.ProjectCreateRequest;
import com.workout.taskmanager.project.dto.ProjectResponse;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.enums.ProjectRole;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
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
public class ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMemberRepository memberRepository;

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request, User user) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Failed to find user with email " + user.getEmail()));
        Project project = new Project();
        project.setOwner(owner);
        project.setName(request.name());
        project.setDescription(request.description());
        project = projectRepository.save(project);
        ProjectMember ownerMembership = new ProjectMember();
        ownerMembership.setProject(project);
        ownerMembership.setUser(owner);
        ownerMembership.setRole(ProjectRole.OWNER);
        memberRepository.save(ownerMembership);
        return ProjectResponse.from(project);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id, User user) {
        Project project = checkMembershipAndGetProject(id, user);
        return ProjectResponse.from(project);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAllProjects(Pageable pageable, User user) {
        return projectRepository.findByUser(user, pageable).map(ProjectResponse::from);
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
}
