package com.rajesh.task_service.repository;

import com.rajesh.task_service.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssigneeUsername(String assigneeUsername);

    Page<Task> findByStatus(String status, Pageable pageable);

    Page<Task> findByPriority(String priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(String status, String priority, Pageable pageable);



}
