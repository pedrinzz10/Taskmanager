package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<TaskResponse> getTasks(String username) {
        return taskRepo.findByUserUsername(username)
                .stream()
                .map(t -> new TaskResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.isCompleted()
                ))
                .toList();
    }

    public TaskResponse create(TaskRequest request, String username) {
        User user = userRepo.findByUsername(username).orElseThrow();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setUser(user);

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