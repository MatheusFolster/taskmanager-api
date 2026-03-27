package com.taskmanager.mapper;

import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.entity.Subtask;
import com.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(target = "progress", expression = "java(calculateProgress(task))")
    TaskResponse toResponse(Task task);

    default int calculateProgress(Task task) {
        List<Subtask> subtasks = task.getSubtasks();
        if (subtasks == null || subtasks.isEmpty()) return 0;
        long completed = subtasks.stream().filter(Subtask::isCompleted).count();
        return (int) Math.round(completed * 100.0 / subtasks.size());
    }
}
