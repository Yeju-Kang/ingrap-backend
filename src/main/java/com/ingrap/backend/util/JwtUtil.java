package com.ingrap.backend.util;

import com.ingrap.backend.module.user.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private SecretKey secretKey;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    public enum TokenType {
        ACCESS, REFRESH
    }

    public JwtUtil(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @PostConstruct
    protected void init() {
        logger.info("🚀 JWT_SECRET 값: {}", secret);
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ 토큰 생성 (유형별로)
    public String generateToken(String subject, TokenType tokenType, long validityInSeconds) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("tokenType", tokenType.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInSeconds * 1000))
                .signWith(secretKey)
                .compact();
    }

    // ✅ 액세스 토큰 생성
    public String generateAccessToken(String email) {
        return generateToken(email, TokenType.ACCESS, tokenValidityInSeconds);
    }

    // ✅ 리프레시 토큰 생성
    public String generateRefreshToken(String userId) {
        return generateToken(userId, TokenType.REFRESH, refreshTokenValidityInSeconds);
    }

    // ✅ 토큰에서 이메일(주체) 추출
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ 토큰에서 만료 날짜 추출
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    // ✅ 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    // ✅ 토큰이 리프레시 토큰인지 검증
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "REFRESH".equals(claims.get("tokenType"));
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("리프레시 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // ✅ 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("지원하지 않는 JWT 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("잘못된 JWT 토큰: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("JWT 서명이 잘못되었습니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT 토큰이 비어있습니다: {}", e.getMessage());
        }
        return false;
    }

    // ✅ Claims 가져오기
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            logger.error("Claims 추출 실패: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }
}
