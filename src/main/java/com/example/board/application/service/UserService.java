package com.example.board.application.service;

import com.example.board.application.dto.UserDto;
import com.example.board.domain.Role;
import com.example.board.domain.User;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /* CRUD */

    // CREATE - 회원가입
    @Transactional
    public void joinUser(UserDto.Request user) {

        boolean isUser = userRepository.existsByUsername(user.getUsername());
        if(isUser) {
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user.toEntity());
    }

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사, 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    // Read
    @Transactional
    public UserDto.Response viewUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        return new UserDto.Response(user);
    }


    @Transactional
    public List<UserDto.Response> viewUsers() {
        return userRepository.findAll().stream()
                .map(UserDto.Response::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void modify(UserDto.Request dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(()->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        //String encPassword = passwordEncoder.encode(dto.getPassword());
        user.modify(dto.getUsername(),dto.getEmail(), dto.getNickname());
    }

}
