package com.workout.taskmanager.project.service;

import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ConflictException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.enums.ProjectRole;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMember addMember(Long projectId, User user, Long addUserId, ProjectRole role) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        boolean isOwner = project.getOwner().getId().equals(user.getId());
        if (!isOwner && !projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new AccessDeniedException("Access denied: not a project owner or member");
        }

        User userToAdd = userRepository.findById(addUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + addUserId));

        if (projectMemberRepository.existsByProjectAndUser(project, userToAdd)) {
            throw new ConflictException("User is already a member of this project");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(userToAdd);
        member.setRole(role);

        return projectMemberRepository.save(member);
    }

    public void removeMember(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
        ProjectMember member = projectMemberRepository
                .findByProjectAndUser(project, user)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in project"));

        projectMemberRepository.delete(member);
    }
}
