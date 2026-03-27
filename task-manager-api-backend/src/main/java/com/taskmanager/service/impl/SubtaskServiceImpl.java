package com.taskmanager.service.impl;

import com.taskmanager.dto.subtask.CreateSubtaskRequest;
import com.taskmanager.dto.subtask.SubtaskResponse;
import com.taskmanager.entity.Subtask;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.exception.ForbiddenException;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.mapper.SubtaskMapper;
import com.taskmanager.repository.SubtaskRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.SubtaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubtaskServiceImpl implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SubtaskMapper subtaskMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SubtaskResponse> listSubtasks(Long taskId, String email) {
        User user = findUser(email);
        Task task = findTaskOwned(taskId, user);
        return subtaskRepository.findByTaskOrderByCreatedAtAsc(task).stream()
                .map(subtaskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SubtaskResponse createSubtask(Long taskId, CreateSubtaskRequest request, String email) {
        User user = findUser(email);
        Task task = findTaskOwned(taskId, user);

        Subtask subtask = Subtask.builder()
                .title(request.title())
                .completed(false)
                .task(task)
                .build();

        return subtaskMapper.toResponse(subtaskRepository.save(subtask));
    }

    @Override
    @Transactional
    public SubtaskResponse completeSubtask(Long id, String email) {
        User user = findUser(email);
        Subtask subtask = findSubtaskOwned(id, user);
        subtask.setCompleted(true);
        return subtaskMapper.toResponse(subtaskRepository.save(subtask));
    }

    @Override
    @Transactional
    public void deleteSubtask(Long id, String email) {
        User user = findUser(email);
        Subtask subtask = findSubtaskOwned(id, user);
        subtaskRepository.delete(subtask);
    }

    private Subtask findSubtaskOwned(Long id, User user) {
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask", id));
        if (!Objects.equals(subtask.getTask().getProject().getUser().getId(), user.getId())) {
            throw new ForbiddenException("Access denied to subtask " + id);
        }
        return subtask;
    }

    private Task findTaskOwned(Long taskId, User user) {
        return taskRepository.findByIdAndProjectUser(taskId, user)
                .orElseThrow(() -> {
                    if (taskRepository.existsById(taskId)) {
                        return new ForbiddenException("Access denied to task " + taskId);
                    }
                    return new ResourceNotFoundException("Task", taskId);
                });
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
