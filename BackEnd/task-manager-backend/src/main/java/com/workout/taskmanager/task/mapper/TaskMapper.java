package com.workout.taskmanager.task.mapper;

import com.workout.taskmanager.task.dto.TaskCreateRequest;
import com.workout.taskmanager.task.dto.TaskUpdateRequest;
import com.workout.taskmanager.task.dto.TaskResponse;
import com.workout.taskmanager.task.entity.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toDto(Task task);

    Task toEntity(TaskCreateRequest request);

    // 🔥 PATCH MAGIC
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskUpdateRequest dto, @MappingTarget Task entity);
}