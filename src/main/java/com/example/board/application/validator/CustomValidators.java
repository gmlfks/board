package com.example.board.application.validator;

import com.example.board.application.dto.UserDto;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component
public class CustomValidators {

    @RequiredArgsConstructor
    @Component
    public static class EmailValidator extends AbstractValidator<UserDto.Request> {
        private final UserRepository userRepository;

        @Override
        protected void doValidate(UserDto.Request dto, Errors errors) {
            if(userRepository.existsByEmail(dto.toEntity().getEmail())) {
                errors.rejectValue("email","이메일 중복 오류","이미 사용중인 이메일입니다.");
            }

        }
    }
    @RequiredArgsConstructor
    @Component
    public static class NicknameValidator extends AbstractValidator<UserDto.Request> {
        private final UserRepository userRepository;

        @Override
        protected void doValidate(UserDto.Request dto, Errors errors) {
            if(userRepository.existsByNickname(dto.toEntity().getNickname())) {
                errors.rejectValue("nickname","닉네임 중복 오류","이미 사용중인 닉네임입니다.");
            }
        }
    }
}
