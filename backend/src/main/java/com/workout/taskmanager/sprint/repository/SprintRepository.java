package com.workout.taskmanager.sprint.repository;

import com.workout.taskmanager.sprint.entity.Sprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SprintRepository extends JpaRepository<Sprint,Long> {

    Page<Sprint> findByProjectId(Long projectId, Pageable pageable);
}
