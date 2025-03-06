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
}
