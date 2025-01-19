package com.example.board.application.config;

import com.example.board.application.security.auth.CustomAuthFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.io.IOException;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomAuthFailureHandler customAuthFailureHandler;

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//
//        return RoleHierarchyImpl.withDefaultRolePrefix()
//                .role("C").implies("B")
//                .role("B").implies("A")
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http

                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**", "/auth/**", "/js/**", "/css/**", "/image/**").permitAll() // 특정 경로는 인증 없이 접근 가능
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated() // 그 외 경로는 인증 필요
                )
                .formLogin((auth) -> auth.loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .failureHandler(customAuthFailureHandler)
                        .defaultSuccessUrl("/welcome")
                        //위에 이제 post page로 데리고 가야 함.
                        .permitAll())
                 .logout((logout)->logout
//                         .logoutUrl("/auth/logout")
                         .logoutSuccessUrl("/auth/login")
                         .addLogoutHandler(new LogoutHandler() {
                             @Override
                             public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                                 HttpSession httpSession = request.getSession();
                                 httpSession.invalidate();
                             }
                         })
                         .logoutSuccessHandler(new LogoutSuccessHandler() {
                             @Override
                             public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                 log.info("로그아웃 성공");
                                 response.sendRedirect("/");
                             }
                         })
                                 .deleteCookies("remember-me"));

        /**
         * // 로그아웃
         *         http.logout(
         *                 logout -> logout
         *                         .logoutUrl("/logout") // 로그아웃 처리 URL
         *                         .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동시킬 페이지
         *                         // 로그아웃 핸들러, 로그아웃 성공 여부와 상관없이 실행된다.
         *                         .addLogoutHandler(new LogoutHandler() {
         *                             @Override
         *                             public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
         *                                 HttpSession httpSession = request.getSession();
         *                                 httpSession.invalidate();
         *                             }
         *                         })
         *                         // 로그아웃 성공 핸들러
         *                         .logoutSuccessHandler(new LogoutSuccessHandler() {
         *                             @Override
         *                             public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         *                                 System.out.println("로그아웃 성공");
         *                                 response.sendRedirect("/login");
         *                             }
         *                         })
         *                         .deleteCookies("remember-me") // 로그아웃 후 쿠키 삭제
         *
         *         );
         */
        http
                 .csrf((auth) -> auth.disable());

        return http.build(); // SecurityFilterChain 반환
    }
    // 이 부분 버전에 따라 바뀜
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
