package com.taskmanager.service;

import com.taskmanager.dto.task.CreateTaskRequest;
import com.taskmanager.dto.task.TaskResponse;
import com.taskmanager.dto.task.UpdateTaskRequest;
import com.taskmanager.dto.task.UpdateTaskStatusRequest;
import com.taskmanager.entity.enums.TaskPriority;
import com.taskmanager.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskResponse> listTasks(Long projectId, String email,
                                 TaskStatus status, TaskPriority priority,
                                 String tag, Pageable pageable);

    TaskResponse getTask(Long id, String email);

    TaskResponse createTask(Long projectId, CreateTaskRequest request, String email);

    TaskResponse updateTask(Long id, UpdateTaskRequest request, String email);

    TaskResponse updateTaskStatus(Long id, UpdateTaskStatusRequest request, String email);

    void deleteTask(Long id, String email);
}
