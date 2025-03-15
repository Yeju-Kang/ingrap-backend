package com.ingrap.backend.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private final Claims claims;

    public JwtAuthenticationToken(UserDetails principal, Claims claims) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.claims = claims;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // JWT는 비밀번호 인증을 하지 않음
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    public Claims getClaims() {
        return claims;
    }

    // ✅ 토큰의 Subject(주체)를 가져오는 메서드
    public String getToken() {
        return claims.getSubject();
    }

    // ✅ 특정 클레임을 가져오는 메서드
    public Object getClaim(String key) {
        return claims.get(key);
    }

    // ✅ 디버깅을 위한 toString 오버라이드
    @Override
    public String toString() {
        return "JwtAuthenticationToken{" +
                "principal=" + principal +
                ", claims=" + claims +
                '}';
    }
}
