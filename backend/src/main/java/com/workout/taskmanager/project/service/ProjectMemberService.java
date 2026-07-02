package com.workout.taskmanager.project.service;

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
                .orElseThrow(() -> new RuntimeException("Project not found"));


        boolean isOwner = project.getOwner().getId().equals(user.getId());
        if (!isOwner && !projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new RuntimeException("Access denied: not a project owner or member");
        }

        User userToAdd = userRepository.findById(addUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (projectMemberRepository.existsByProjectAndUser(project, userToAdd)) {
            throw new RuntimeException("User already member of project");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRole(role);

        return projectMemberRepository.save(member);
    }

    public void removeMember(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        ProjectMember member = projectMemberRepository
                .findByProjectAndUser(project, user)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        projectMemberRepository.delete(member);
    }
}
