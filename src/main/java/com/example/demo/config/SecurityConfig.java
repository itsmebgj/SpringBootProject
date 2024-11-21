package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authz -> authz
                .requestMatchers("/signup").permitAll() // 회원가입은 누구나 접근 가능
                .anyRequest().permitAll() // 모든 요청 허용
            )
            .logout(logout -> logout
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // 로그아웃을 POST 방식으로 처리
                .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트할 URL
            )
            .csrf().disable(); // CSRF를 비활성화할 필요가 있으면 설정 (로그아웃을 POST 방식으로 설정할 때 필요)

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
