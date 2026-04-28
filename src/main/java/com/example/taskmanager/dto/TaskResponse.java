package com.example.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}