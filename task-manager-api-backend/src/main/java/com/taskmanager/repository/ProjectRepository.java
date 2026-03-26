package com.taskmanager.repository;

import com.taskmanager.entity.Project;
import com.taskmanager.entity.User;
import com.taskmanager.entity.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByUser(User user, Pageable pageable);

    Page<Project> findByUserAndStatus(User user, ProjectStatus status, Pageable pageable);

    Optional<Project> findByIdAndUser(Long id, User user);
}
