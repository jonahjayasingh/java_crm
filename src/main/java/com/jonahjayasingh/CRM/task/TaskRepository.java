package com.jonahjayasingh.CRM.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByAssignedToId(Long userId);

    long countByAssignedToIdAndStatusNot(Long userId, TaskStatus status);
}
