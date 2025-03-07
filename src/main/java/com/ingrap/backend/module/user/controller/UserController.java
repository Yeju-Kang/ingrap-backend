package com.ingrap.backend.module.user.controller;

import com.ingrap.backend.module.user.entity.User;
import com.ingrap.backend.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")  // ✅ 회원가입 엔드포인트 변경
    public ResponseEntity<?> signupUser(@RequestBody Map<String, String> userRequest) {
        String username = userRequest.get("username");
        String email = userRequest.get("email");
        String password = userRequest.get("password");

        try {
            User user = userService.signupUser(username, email, password); // ✅ signupUser() 호출
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        String token = userService.loginUser(email, password);

        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유지

        response.addCookie(cookie);
        return ResponseEntity.ok("로그인 성공");
    }

}
