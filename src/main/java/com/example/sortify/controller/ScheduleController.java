package com.example.sortify.controller;

import com.example.sortify.config.JwtUtil;
import com.example.sortify.dto.ScheduleDTO;
import com.example.sortify.entity.User;
import com.example.sortify.repository.UserRepository;
import com.example.sortify.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ScheduleController(ScheduleService scheduleService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.scheduleService = scheduleService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // 일정 생성
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO, @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);

        // ✅ DB에서 영속된 User 객체 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduleService.createSchedule(user, scheduleDTO);
    }

    // 사용자 이메일로 일정 조회
    @GetMapping
    public List<ScheduleDTO> getSchedulesByUser(@RequestHeader("Authorization") String authorization) {
        // JWT 토큰에서 이메일 추출
        String token = authorization.substring(7);  // Bearer 제외하고 토큰만 가져오기
        String email = jwtUtil.extractEmail(token);

        User user = new User();
        user.setEmail(email);

        return scheduleService.getSchedulesByUser(user);
    }
}
