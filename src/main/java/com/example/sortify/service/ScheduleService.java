package com.example.sortify.service;

import com.example.sortify.dto.ScheduleDTO;
import com.example.sortify.dto.ToDoDTO;
import com.example.sortify.entity.Schedule;
import com.example.sortify.entity.ToDo;
import com.example.sortify.entity.User;
import com.example.sortify.repository.ScheduleRepository;
import com.example.sortify.repository.ToDoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ToDoRepository toDoRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ToDoRepository toDoRepository) {
        this.scheduleRepository = scheduleRepository;
        this.toDoRepository = toDoRepository;
    }

    // ✅ 일정 생성 (단일 + 반복 일정 처리)
    @Transactional
    public ScheduleDTO createSchedule(User user, ScheduleDTO scheduleDTO) {
        if (scheduleDTO.isRecurring()) {
            List<ScheduleDTO> createdSchedules = new ArrayList<>();
            LocalDateTime current = scheduleDTO.getStartTime();
            LocalDateTime end = scheduleDTO.getRepeatUntil();

            while (!current.isAfter(end)) {
                DayOfWeek currentDay = current.getDayOfWeek();
                if (scheduleDTO.getRecurringDays().contains(currentDay.name())) {
                    Schedule schedule = new Schedule();
                    schedule.setUser(user);
                    schedule.setTitle(scheduleDTO.getTitle());
                    schedule.setStartTime(current);
                    schedule.setEndTime(current.plusMinutes(
                            Duration.between(scheduleDTO.getStartTime(), scheduleDTO.getEndTime()).toMinutes()
                    ));
                    schedule.setRecurring(true);
                    schedule.setRecurringDays(scheduleDTO.getRecurringDays());
                    schedule.setRepeatUntil(scheduleDTO.getRepeatUntil());

                    // ✅ ToDo 처리
                    if (scheduleDTO.getToDos() != null && !scheduleDTO.getToDos().isEmpty()) {
                        List<ToDo> toDoList = scheduleDTO.getToDos().stream().map(toDoDTO -> {
                            ToDo toDo = new ToDo();
                            toDo.setTask(toDoDTO.getTask());
                            toDo.setCompleted(toDoDTO.isCompleted());
                            toDo.setCreatedAt(toDoDTO.getCreatedAt());
                            toDo.setUser(user);
                            toDo.setSchedule(schedule);
                            return toDo;
                        }).collect(Collectors.toList());
                        schedule.setToDos(toDoList);
                    }

                    Schedule saved = scheduleRepository.save(schedule);
                    createdSchedules.add(convertToDTO(saved));
                }
                current = current.plusDays(1);
            }

            return createdSchedules.get(0); // 첫 번째 반복 일정 반환
        } else {
            Schedule schedule = new Schedule();
            schedule.setUser(user);
            schedule.setTitle(scheduleDTO.getTitle());
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());
            schedule.setRecurring(false);

            // ✅ ToDo 처리
            if (scheduleDTO.getToDos() != null && !scheduleDTO.getToDos().isEmpty()) {
                List<ToDo> toDoList = scheduleDTO.getToDos().stream().map(toDoDTO -> {
                    ToDo toDo = new ToDo();
                    toDo.setTask(toDoDTO.getTask());
                    toDo.setCompleted(toDoDTO.isCompleted());
                    toDo.setCreatedAt(toDoDTO.getCreatedAt());
                    toDo.setUser(user);
                    toDo.setSchedule(schedule);
                    return toDo;
                }).collect(Collectors.toList());
                schedule.setToDos(toDoList);
            }

            Schedule savedSchedule = scheduleRepository.save(schedule);
            return convertToDTO(savedSchedule);
        }
    }

    // ✅ 사용자 이메일로 일정 전체 조회
    public List<ScheduleDTO> getSchedulesByUser(User user) {
        return scheduleRepository.findByUser_Email(user.getEmail()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Schedule → ScheduleDTO 변환
    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setTitle(schedule.getTitle());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setRecurring(schedule.isRecurring());
        dto.setRecurringDays(schedule.getRecurringDays());
        dto.setRepeatUntil(schedule.getRepeatUntil());

        List<ToDoDTO> toDoDTOs = schedule.getToDos().stream()
                .map(this::convertToDoToDTO)
                .collect(Collectors.toList());
        dto.setToDos(toDoDTOs);

        return dto;
    }

    // ✅ ToDo → ToDoDTO 변환
    private ToDoDTO convertToDoToDTO(ToDo toDo) {
        ToDoDTO dto = new ToDoDTO();
        dto.setId(toDo.getId());
        dto.setTask(toDo.getTask());
        dto.setCompleted(toDo.isCompleted());
        dto.setCreatedAt(toDo.getCreatedAt());
        return dto;
    }
}
