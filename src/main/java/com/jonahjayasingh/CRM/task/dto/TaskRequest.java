package com.jonahjayasingh.CRM.task.dto;

import java.time.LocalDate;

import com.jonahjayasingh.CRM.task.TaskPriority;
import com.jonahjayasingh.CRM.task.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private Long assignedToUserId;
}
