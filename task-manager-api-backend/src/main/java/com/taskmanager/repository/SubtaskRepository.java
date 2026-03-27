package com.taskmanager.repository;

import com.taskmanager.entity.Subtask;
import com.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    List<Subtask> findByTaskOrderByCreatedAtAsc(Task task);

    Optional<Subtask> findByIdAndTask(Long id, Task task);
}
