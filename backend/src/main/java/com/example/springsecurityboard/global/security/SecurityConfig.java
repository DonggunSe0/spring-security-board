package com.example.springsecurityboard.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 프론트 localhost:3000에서 오는 요청을 허용하기 위해 CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 지금은 JWT/Cookie 인증을 아직 만들지 않았기 때문에
                // 학습 초기 단계에서는 CSRF를 잠시 꺼둔다.
                .csrf(csrf -> csrf.disable())

                // 요청별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 서버 상태 확인 API는 로그인 없이 접근 가능
                        .requestMatchers("/api/v1/health").permitAll()

                        // 아직 회원가입/로그인/JWT를 만들기 전이므로
                        // 나머지 요청도 일단 모두 허용
                        .anyRequest().permitAll()
                )

                // 기본 로그인 폼 비활성화
                // 우리는 나중에 직접 로그인 API를 만들 예정이기 때문
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                // 브라우저 팝업 로그인 방식은 사용하지 않을 예정
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 개발 서버 주소 허용
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 허용할 요청 헤더
        configuration.setAllowedHeaders(List.of("*"));

        // 쿠키를 포함한 요청 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 모든 API 경로에 CORS 설정 적용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

