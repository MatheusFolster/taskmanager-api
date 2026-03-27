package com.taskmanager.service;

import com.taskmanager.dto.subtask.CreateSubtaskRequest;
import com.taskmanager.dto.subtask.SubtaskResponse;

import java.util.List;

public interface SubtaskService {

    List<SubtaskResponse> listSubtasks(Long taskId, String email);

    SubtaskResponse createSubtask(Long taskId, CreateSubtaskRequest request, String email);

    SubtaskResponse completeSubtask(Long id, String email);

    void deleteSubtask(Long id, String email);
}
