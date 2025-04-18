package com.ingrap.backend.module.user.controller;

import com.ingrap.backend.module.user.entity.User;
import com.ingrap.backend.module.user.service.UserService;
import com.ingrap.backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingrap.backend.module.user.dto.UserSignupRequest;
import jakarta.validation.Valid;


import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * ✅ 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody @Valid UserSignupRequest request) {
        try {
            User user = userService.signupUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getUserType() // null일 경우 service에서 INDIVIDUAL 처리
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "회원가입 실패", "message", e.getMessage()));
        }
    }


    /**
     * ✅ 로그인 (액세스 토큰 & 리프레시 토큰 발급)
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            // ✅ accessToken + refreshToken 생성
            Map<String, String> tokens = userService.loginUser(email, password);

            // ✅ access_token 쿠키 설정
            setCookie(response, "access_token", tokens.get("accessToken"), 7 * 24 * 60 * 60);

            // ✅ refresh_token 쿠키 설정
            setCookie(response, "refresh_token", tokens.get("refreshToken"), 14 * 24 * 60 * 60);

            return ResponseEntity.ok(Map.of("message", "로그인 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인 실패", "message", e.getMessage()));
        }
    }

    /**
     * ✅ 로그아웃 (토큰 블랙리스트 등록 및 쿠키 삭제)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {

        if (accessToken != null && refreshToken != null) {
            userService.logoutUser(accessToken, refreshToken);
        }

        deleteCookie(response, "access_token");
        deleteCookie(response, "refresh_token");

        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

    /**
     * ✅ 리프레시 토큰으로 액세스 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> requestBody) {
        String refreshToken = requestBody.get("refreshToken");

        try {
            String newAccessToken = userService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * ✅ 공통 Cookie 설정
     */
    private void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    /**
     * ✅ 공통 Cookie 삭제
     */
    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
