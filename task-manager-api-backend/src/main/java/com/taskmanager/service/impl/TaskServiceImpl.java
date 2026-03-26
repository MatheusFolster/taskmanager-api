package com.taskmanager.service.impl;

import com.taskmanager.dto.task.CreateTaskRequest;
import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.dto.task.UpdateTaskRequest;
import com.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.taskmanager.entity.Project;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.util.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> listTasks(Long projectId, String email,
                                        TaskStatus status, TaskPriority priority,
                                        String tag, Pageable pageable) {
        User user = findUser(email);
        Project project = findProjectOwned(projectId, user);

        Specification<Task> spec = Specification
                .where(TaskSpecification.belongsToProject(project))
                .and(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.hasTag(tag));

        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTask(Long id, String email) {
        User user = findUser(email);
        return taskMapper.toResponse(findTaskOwned(id, user));
    }

    @Override
    @Transactional
    public TaskResponse createTask(Long projectId, CreateTaskRequest request, String email) {
        User user = findUser(email);
        Project project = findProjectOwned(projectId, user);

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .tags(request.tags() != null ? new LinkedHashSet<>(request.tags()) : new LinkedHashSet<>())
                .project(project)
                .build();

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, UpdateTaskRequest request, String email) {
        User user = findUser(email);
        Task task = findTaskOwned(id, user);

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        task.getTags().clear();
        if (request.tags() != null) {
            task.getTags().addAll(request.tags());
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request, String email) {
        User user = findUser(email);
        Task task = findTaskOwned(id, user);
        task.setStatus(request.status());
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id, String email) {
        User user = findUser(email);
        Task task = findTaskOwned(id, user);
        taskRepository.delete(task);
    }

    private Project findProjectOwned(Long projectId, User user) {
        return projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> {
                    if (projectRepository.existsById(projectId)) {
                        return new ForbiddenException("Access denied to project " + projectId);
                    }
                    return new ResourceNotFoundException("Project", projectId);
                });
    }

    private Task findTaskOwned(Long id, User user) {
        return taskRepository.findByIdAndProjectUser(id, user)
                .orElseThrow(() -> {
                    if (taskRepository.existsById(id)) {
                        return new ForbiddenException("Access denied to task " + id);
                    }
                    return new ResourceNotFoundException("Task", id);
                });
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
