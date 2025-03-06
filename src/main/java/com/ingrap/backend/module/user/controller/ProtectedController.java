package com.ingrap.backend.module.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping
    public String getProtectedData() {
        return "이 데이터는 JWT 인증된 사용자만 접근 가능합니다!";
    }
}
