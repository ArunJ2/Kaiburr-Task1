package com.kaiburr.task_manager.controller;

import com.kaiburr.task_manager.model.Task;
import com.kaiburr.task_manager.model.TaskExecution;
import com.kaiburr.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Allow React frontend to connect
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    // GET all tasks or single task by ID
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        try {
            if (id != null) {
                Optional<Task> task = taskService.getTaskById(id);
                if (task.isEmpty()) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Task not found with ID: " + id);
                    response.put("status", "404");
                    return ResponseEntity.status(404).body(response);
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Task retrieved successfully");
                response.put("task", task.get());
                response.put("status", "200");
                return ResponseEntity.ok(response);
            }
            
            List<Task> tasks = taskService.getAllTasks();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Tasks retrieved successfully");
            response.put("tasks", tasks);
            response.put("count", tasks.size());
            response.put("status", "200");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error retrieving tasks: " + e.getMessage());
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // PUT create or update task
    @PutMapping
    public ResponseEntity<?> createOrUpdateTask(@RequestBody Task task) {
        try {
            Task savedTask = taskService.createOrUpdateTask(task);
            
            // Return success message with the task
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Task saved successfully");
            response.put("task", savedTask);
            response.put("status", "200");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Validation error: " + e.getMessage());
            response.put("status", "400");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error saving task: " + e.getMessage());
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // DELETE task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable String id) {
        try {
            // Check if task exists before deleting
            Optional<Task> existingTask = taskService.getTaskById(id);
            if (existingTask.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Task not found with ID: " + id);
                response.put("status", "404");
                return ResponseEntity.status(404).body(response);
            }
            
            taskService.deleteTask(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Task deleted successfully");
            response.put("deletedId", id);
            response.put("status", "200");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error deleting task: " + e.getMessage());
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // GET tasks by name search
    @GetMapping("/search")
    public ResponseEntity<?> searchTasksByName(@RequestParam String name) {
        try {
            List<Task> tasks = taskService.findTasksByName(name);
            if (tasks.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No tasks found containing: " + name);
                response.put("status", "404");
                return ResponseEntity.status(404).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Search completed successfully");
            response.put("tasks", tasks);
            response.put("count", tasks.size());
            response.put("searchTerm", name);
            response.put("status", "200");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error searching tasks: " + e.getMessage());
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // PUT execute command for a task
    @PutMapping("/{id}/execute")
    public ResponseEntity<?> executeCommand(@PathVariable String id) {
        try {
            TaskExecution execution = taskService.executeCommand(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Command executed successfully");
            response.put("execution", execution);
            response.put("status", "200");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Task not found with ID: " + id);
            response.put("status", "404");
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error executing command: " + e.getMessage());
            response.put("status", "500");
            return ResponseEntity.status(500).body(response);
        }
    }
}