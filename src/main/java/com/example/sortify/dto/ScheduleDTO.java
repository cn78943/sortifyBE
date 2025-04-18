package com.example.sortify.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleDTO {

    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ToDoDTO> toDos;

    // 반복 설정 관련 필드 추가
    private boolean recurring;                     // 반복 여부
    private List<String> recurringDays;            // 반복 요일 (예: ["MONDAY", "WEDNESDAY"])
    private LocalDateTime repeatUntil;             // 반복 종료일

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<ToDoDTO> getToDos() {
        return toDos;
    }

    public void setToDos(List<ToDoDTO> toDos) {
        this.toDos = toDos;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public List<String> getRecurringDays() {
        return recurringDays;
    }

    public void setRecurringDays(List<String> recurringDays) {
        this.recurringDays = recurringDays;
    }

    public LocalDateTime getRepeatUntil() {
        return repeatUntil;
    }

    public void setRepeatUntil(LocalDateTime repeatUntil) {
        this.repeatUntil = repeatUntil;
    }
}
