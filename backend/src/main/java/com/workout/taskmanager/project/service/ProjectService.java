package com.workout.taskmanager.project.service;

import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.project.dto.ProjectCreateRequest;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;

    public Project createProject(ProjectCreateRequest request, User user) {
        User owner = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Failed to find user with email " + user.getEmail()));
        Project project = new Project();
        project.setOwner(owner);
        project.setName(request.name());
        project.setDescription(request.description());
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    public Page<Project> getAllProjects(Pageable pageable, User user) {
        return projectRepository.findByUser(user, pageable);
    }
}
