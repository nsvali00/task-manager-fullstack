package com.workout.taskmanager.sprint.service;

import com.workout.taskmanager.common.exceptions.AccessDeniedException;
import com.workout.taskmanager.common.exceptions.ResourceNotFoundException;
import com.workout.taskmanager.project.entity.Project;
import com.workout.taskmanager.project.entity.ProjectMember;
import com.workout.taskmanager.project.repository.ProjectMemberRepository;
import com.workout.taskmanager.project.repository.ProjectRepository;
import com.workout.taskmanager.sprint.dto.SprintCreateRequest;
import com.workout.taskmanager.sprint.dto.SprintResponse;
import com.workout.taskmanager.sprint.dto.SprintUpdateRequest;
import com.workout.taskmanager.sprint.entity.Sprint;
import com.workout.taskmanager.sprint.enums.SprintStatus;
import com.workout.taskmanager.sprint.repository.SprintRepository;
import com.workout.taskmanager.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class SprintService {
    private static final Logger log = LoggerFactory.getLogger(SprintService.class);
    private SprintRepository sprintRepository;
    private ProjectRepository projectRepository;
    private ProjectMemberRepository memberRepository;

    public SprintResponse createSprint(Long projectId, SprintCreateRequest request, User currentUser) {
        log.info("Creating new sprint with name {}", request.name());
        Project project  = checkMembershipAndGetProject(projectId, currentUser);

        Sprint sprint = new Sprint();
        sprint.setName(request.name());
        sprint.setProject(project);
        sprint.setStatus(SprintStatus.PLANNED);
        Sprint savedSprint = sprintRepository.save(sprint);
        return SprintResponse.from(savedSprint);
    }

    public Page<SprintResponse> getSprintsForProject(Pageable pageable, Long projectId, User currentUser) {
        Project project  = checkMembershipAndGetProject(projectId, currentUser);
        Page<Sprint> allSprints = sprintRepository.findByProjectId(projectId, pageable);
        return allSprints.map(SprintResponse::from);
    }

    public SprintResponse getSprintById(Long sprintId, User currentUser){
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id " + sprintId));
        checkMembershipAndGetProject(sprint.getProject().getId(), currentUser);
        return SprintResponse.from(sprint);
    }

    public SprintResponse updateSprint(Long sprintId, SprintUpdateRequest request, User currentUser){
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id " + sprintId));
        checkMembershipAndGetProject(sprint.getProject().getId(), currentUser);

        if(request.status() !=null){
            sprint.setStatus(request.status());
        }
        if(!ObjectUtils.isEmpty(request.name())){
            sprint.setName(request.name());
        }
        Sprint modifiedSprint = sprintRepository.save(sprint);
        return SprintResponse.from(modifiedSprint);

    }

    public void deleteSprint(Long sprintId, User currentUser){
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(() -> new ResourceNotFoundException("Failed to find sprint with id " + sprintId));
        checkMembershipAndGetProject(sprint.getProject().getId(), currentUser);
        sprintRepository.delete(sprint);

    }


    private @NonNull Project checkMembershipAndGetProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        if (!memberRepository.existsByProjectAndUser(project, currentUser)) {
            throw new AccessDeniedException("Not a project member");
        }
        return project;
    }





}
