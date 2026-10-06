package com.jonahjayasingh.CRM.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonahjayasingh.CRM.task.dto.TaskRequest;
import com.jonahjayasingh.CRM.task.dto.TaskResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request, username));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(taskService.updateTask(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        taskService.deleteTask(id, username);
        return ResponseEntity.ok("Task deleted successfully with ID: " + id);
    }
}
