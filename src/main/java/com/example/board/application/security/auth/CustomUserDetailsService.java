package com.example.board.application.security.auth;

import com.example.board.application.dto.UserDto;
import com.example.board.domain.User;
import com.example.board.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession session;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()
        -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : "+username));

        session.setAttribute("user",new UserDto.Response(user));

        return new CustomUserDetails(user);
    }
}
