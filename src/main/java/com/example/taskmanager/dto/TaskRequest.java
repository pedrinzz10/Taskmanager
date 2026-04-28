package com.example.taskmanager.dto;

public class TaskRequest {

    private String title;
    private String description;
    private boolean completed;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}