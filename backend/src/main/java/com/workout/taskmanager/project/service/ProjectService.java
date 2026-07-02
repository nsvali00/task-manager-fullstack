package com.workout.taskmanager.project.service;

import com.workout.taskmanager.project.dto.ProjectCreateRequest;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.user.entity.CustomUserDetails;
import com.workout.taskmanager.user.entity.User;
import com.workout.taskmanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;

    public Project createProject(ProjectCreateRequest request, User user) {
        // check if user exists
        User owner = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Failed to find user with email " + user.getEmail()));
        // create if everything ok and save
        Project project = new Project();
        project.setOwner(owner);
        project.setName(request.name());
        project.setDescription(request.description());
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id){
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Failed to find project with id " + id));
    }
    public Page<Project> getAllProjects(Pageable pageable){
        return projectRepository.findAll(pageable);
    }
}
