package com.jonahjayasingh.CRM.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jonahjayasingh.CRM.task.TaskPriority;
import com.jonahjayasingh.CRM.task.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Long assignedToUserId;
    private String assignedToUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
