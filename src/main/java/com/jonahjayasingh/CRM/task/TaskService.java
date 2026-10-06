package com.jonahjayasingh.CRM.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonahjayasingh.CRM.audit.AuditLogService;
import com.jonahjayasingh.CRM.exception.ResourceNotFoundException;
import com.jonahjayasingh.CRM.task.dto.TaskRequest;
import com.jonahjayasingh.CRM.task.dto.TaskResponse;
import com.jonahjayasingh.CRM.user.User;
import com.jonahjayasingh.CRM.user.UserRepository;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public TaskResponse createTask(TaskRequest request, String username) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            task.setAssignedTo(user);
        }

        Task savedTask = taskRepository.save(task);
        auditLogService.log("CREATE", "Task", savedTask.getId(), username, "Created task: " + savedTask.getTitle());
        return mapToResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        task.setDueDate(request.getDueDate());

        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToUserId()));
            task.setAssignedTo(user);
        }

        Task updatedTask = taskRepository.save(task);
        auditLogService.log("UPDATE", "Task", updatedTask.getId(), username, "Updated task: " + updatedTask.getTitle());
        return mapToResponse(updatedTask);
    }

    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
        auditLogService.log("DELETE", "Task", id, username, "Deleted task: " + task.getTitle());
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .assignedToUserId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToUserName(task.getAssignedTo() != null ? task.getAssignedTo().getName() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
