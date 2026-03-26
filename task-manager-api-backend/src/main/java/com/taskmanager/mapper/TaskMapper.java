package com.taskmanager.mapper;

import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    TaskResponse toResponse(Task task);
}
