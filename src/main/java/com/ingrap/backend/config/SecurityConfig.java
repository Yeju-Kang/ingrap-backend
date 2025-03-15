package com.ingrap.backend.config;

import com.ingrap.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // ✅ 최신 방식으로 CORS 적용
                .csrf(csrf -> csrf.disable())  // ✅ CSRF 비활성화 (API 요청 허용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signup", "/api/users/login", "/api/users/refresh").permitAll()  // ✅ 회원가입 & 로그인은 인증 없이 허용
                        .requestMatchers("/api/protected").authenticated()  // ✅ 로그인한 사용자만 접근 가능
                        .anyRequest().authenticated()  // ✅ 그 외 모든 요청은 인증 필요
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // ✅ 세션 사용 X
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // ✅ JWT 필터 적용

        return http.build();
    }

    // ✅ CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // ✅ 허용할 Origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // ✅ 모든 헤더 허용
        configuration.setAllowCredentials(true); // ✅ 쿠키 및 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // ✅ 비밀번호 암호화
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
