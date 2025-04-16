package com.example.sortify.dto;

import java.time.LocalDateTime;

public class ToDoDTO {
    private Integer id;
    private String task;
    private boolean status;
    private LocalDateTime createdAt;

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean completed() {
        return status;
    }

    public void setCompleted(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
