package com.taskmanager.mapper;

import com.taskmanager.dto.project.ProjectResponse;
import com.taskmanager.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toResponse(Project project);
}
