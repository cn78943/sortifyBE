package com.example.sortify.service;

import com.example.sortify.entity.User;
import com.example.sortify.dto.UserDTO;
import com.example.sortify.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    // PasswordEncoder 제거
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword()); // 암호화 없이 저장
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
