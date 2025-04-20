package com.ingrap.backend.security;

import com.ingrap.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        logger.info("요청된 경로: {}" + requestPath);

        // 1. 인증 예외 경로 허용
        if (isExcludedPath(requestPath)) {
            logger.info("예외 엔드포인트, 필터 통과: {}" + requestPath);
            chain.doFilter(request, response);
            return;
        }

        // 2. 토큰 추출
        String token = resolveToken(request);
        if (!StringUtils.hasText(token)) {
            logger.warn("Authorization 헤더와 access_token 쿠키가 없습니다.");
            chain.doFilter(request, response);
            return;
        }

        logger.info("검사할 토큰: {}" + token);

        try {
            // 블랙리스트 검사
            if (jwtUtil.isTokenBlacklisted(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트에 등록된 토큰입니다.");
                return;
            }

            // 토큰 유효성 검사
            if (!jwtUtil.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }

            // refresh 토큰은 거부
            if (jwtUtil.isRefreshToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "액세스 토큰만 허용됩니다.");
                return;
            }

            // 사용자 정보 세팅
            String email = jwtUtil.extractEmail(token);
            Claims claims = jwtUtil.getClaims(token);
            Long userId = jwtUtil.extractUserId(token);

            logger.info("토큰에서 추출한 이메일: {}" + email);

            CustomUserDetails userDetails = new CustomUserDetails(userId, email);
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(userDetails, claims);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            logger.info("인증 완료: {}" + email);

        } catch (ExpiredJwtException e) {
            logger.error("토큰 만료", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
            return;
        } catch (JwtException e) {
            logger.error("잘못된 JWT 토큰", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 JWT 토큰입니다.");
            return;
        } catch (Exception e) {
            logger.error("서버 오류", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 로그인/회원가입 등 인증 없이 접근 가능한 경로 지정
     */
    private boolean isExcludedPath(String path) {
        return path.startsWith("/api/users/signup") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/users/refresh") ||
                path.startsWith("/api/spaces/guest") ||
                path.startsWith("/api/uploadlink");
    }

    /**
     * 헤더 또는 쿠키에서 JWT 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
