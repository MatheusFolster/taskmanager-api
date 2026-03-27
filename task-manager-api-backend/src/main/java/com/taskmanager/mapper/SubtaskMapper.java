package com.taskmanager.mapper;

import com.taskmanager.dto.subtask.SubtaskResponse;
import com.taskmanager.entity.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubtaskMapper {

    @Mapping(source = "task.id", target = "taskId")
    SubtaskResponse toResponse(Subtask subtask);
}
