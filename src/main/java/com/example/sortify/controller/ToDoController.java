package com.example.sortify.controller;

import com.example.sortify.config.CustomUserDetails;
import com.example.sortify.dto.ToDoDTO;
import com.example.sortify.entity.Schedule;
import com.example.sortify.entity.ToDo;
import com.example.sortify.entity.User;
import com.example.sortify.repository.ScheduleRepository;
import com.example.sortify.repository.ToDoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoRepository toDoRepository;
    private final ScheduleRepository scheduleRepository;

    public ToDoController(ToDoRepository toDoRepository, ScheduleRepository scheduleRepository) {
        this.toDoRepository = toDoRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // ✅ 로그인한 사용자의 할 일 목록 조회
    @GetMapping
    public List<ToDoDTO> getAllToDos(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUser().getEmail();

        return toDoRepository.findByUser_Email(email).stream()
                .map(toDo -> {
                    ToDoDTO dto = new ToDoDTO();
                    dto.setId(toDo.getId());
                    dto.setTask(toDo.getTask());
                    dto.setCompleted(toDo.isCompleted());
                    dto.setCreatedAt(toDo.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ✅ 새 할 일 추가
    @PostMapping
    public ToDoDTO addToDo(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestBody ToDoDTO toDoDTO) {

        User user = userDetails.getUser();

        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setTask(toDoDTO.getTask());
        toDo.setCompleted(toDoDTO.isCompleted());
        toDo.setCreatedAt(LocalDateTime.now());

        if (toDoDTO.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(toDoDTO.getScheduleId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
            toDo.setSchedule(schedule);
        }

        ToDo savedToDo = toDoRepository.save(toDo);

        ToDoDTO dto = new ToDoDTO();
        dto.setId(savedToDo.getId());
        dto.setTask(savedToDo.getTask());
        dto.setCompleted(savedToDo.isCompleted());
        dto.setCreatedAt(savedToDo.getCreatedAt());

        return dto;
    }

    // ✅ 할 일 수정
    @PutMapping("/{id}")
    public ToDoDTO updateToDo(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long id,
                              @RequestBody ToDoDTO toDoDTO) {

        User user = userDetails.getUser();

        ToDo existingToDo = toDoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDo not found"));

        if (!existingToDo.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        existingToDo.setTask(toDoDTO.getTask());
        existingToDo.setCompleted(toDoDTO.isCompleted());

        ToDo updatedToDo = toDoRepository.save(existingToDo);

        ToDoDTO dto = new ToDoDTO();
        dto.setId(updatedToDo.getId());
        dto.setTask(updatedToDo.getTask());
        dto.setCompleted(updatedToDo.isCompleted());
        dto.setCreatedAt(updatedToDo.getCreatedAt());

        return dto;
    }

    // ✅ 할 일 삭제
    @DeleteMapping("/{id}")
    public void deleteToDo(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @PathVariable Long id) {

        User user = userDetails.getUser();

        ToDo toDo = toDoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDo not found"));

        if (!toDo.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        toDoRepository.delete(toDo);
    }

    // 완료 여부 반영 api
    @PatchMapping("/{id}/completed")
    public ToDoDTO updateCompletedStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long id,
                                         @RequestBody ToDoDTO toDoDTO) {

        User user = userDetails.getUser();

        ToDo existingToDo = toDoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDo not found"));

        if (!existingToDo.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        existingToDo.setCompleted(toDoDTO.isCompleted());
        ToDo updatedToDo = toDoRepository.save(existingToDo);

        ToDoDTO dto = new ToDoDTO();
        dto.setId(updatedToDo.getId());
        dto.setTask(updatedToDo.getTask());
        dto.setCompleted(updatedToDo.isCompleted());
        dto.setCreatedAt(updatedToDo.getCreatedAt());
        if (updatedToDo.getSchedule() != null) {
            dto.setScheduleId(updatedToDo.getSchedule().getId());
        }

        return dto;
    }

}

