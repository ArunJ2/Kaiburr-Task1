package com.kaiburr.task_manager.service;

import com.kaiburr.task_manager.model.Task;
import com.kaiburr.task_manager.model.TaskExecution;
import com.kaiburr.task_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private CommandValidationService validationService;
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }
    
    public Task createOrUpdateTask(Task task) {
        if (!validationService.isCommandSafe(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected");
        }
        return taskRepository.save(task);
    }
    
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }
    
    public List<Task> findTasksByName(String name) {
        return taskRepository.findByNameContaining(name);
    }
    
    public TaskExecution executeCommand(String taskId) {
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    if (taskOptional.isEmpty()) {
        throw new IllegalArgumentException("Task not found");
    }
    
    Task task = taskOptional.get();
    TaskExecution execution = new TaskExecution();
    execution.setStartTime(new Date());
    
    try {
        // Split command for proper execution (handles spaces in commands)
        String[] commandArray;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Windows: use cmd.exe to execute commands
            commandArray = new String[]{"cmd.exe", "/c", task.getCommand()};
        } else {
            // Linux/Mac: use bash to execute commands
            commandArray = new String[]{"bash", "-c", task.getCommand()};
        }
        
        Process process = Runtime.getRuntime().exec(commandArray);
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
        
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        // Also read error stream to capture any errors
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(process.getErrorStream()));
        while ((line = errorReader.readLine()) != null) {
            output.append("ERROR: ").append(line).append("\n");
        }
        
        int exitCode = process.waitFor();
        execution.setEndTime(new Date());
        execution.setOutput(output.toString());
        
        // Add execution to task
        task.getTaskExecutions().add(execution);
        taskRepository.save(task);
        
    } catch (Exception e) {
        execution.setEndTime(new Date());
        execution.setOutput("Error executing command: " + e.getMessage());
        // Still save the execution with error info
        task.getTaskExecutions().add(execution);
        taskRepository.save(task);
    }
    
    return execution;
}
}