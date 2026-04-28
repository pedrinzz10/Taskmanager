package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<TaskResponse> getTasks() {
        return taskRepo.findAll()
                .stream()
                .map(t -> new TaskResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.isCompleted()
                ))
                .toList();
    }

    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);

        Task saved = taskRepo.save(task);

        return new TaskResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.isCompleted()
        );
    }

    public Optional<Task> getById(Long id) {
        return taskRepo.findById(id);
    }

    public Task update(Long id, TaskRequest request) {
        Task task = taskRepo.findById(id)
                .orElseThrow();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        return taskRepo.save(task);
    }

    public void delete(Long id) {
        taskRepo.deleteById(id);
    }
}
