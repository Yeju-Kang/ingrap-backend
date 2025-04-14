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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

        if (isExcludedPath(requestPath)) {
            logger.info("예외 엔드포인트, 필터 통과: {}" + requestPath);
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        if (!StringUtils.hasText(token)) {
            logger.warn("Authorization 헤더와 access_token 쿠키가 없습니다.");
            chain.doFilter(request, response); // 또는 return 으로 막을 수도 있음
            return;
        }

        logger.info("검사할 토큰: {}" + token);

        try {
            if (jwtUtil.isTokenBlacklisted(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "블랙리스트에 등록된 토큰입니다.");
                return;
            }

            if (!jwtUtil.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }

            if (jwtUtil.isRefreshToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "액세스 토큰만 허용됩니다.");
                return;
            }

            String email = jwtUtil.extractEmail(token);
            Claims claims = jwtUtil.getClaims(token);
            logger.info("토큰에서 추출한 이메일: {}" + email);

            Long userId = jwtUtil.extractUserId(token); // 토큰에서 사용자 ID 추출

            CustomUserDetails userDetails = new CustomUserDetails(userId, email);
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(userDetails, claims);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.info("인증 완료: {}" + email);

        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
            return;
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "잘못된 JWT 토큰입니다.");
            return;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
            return;
        }

        chain.doFilter(request, response);
    }


    private boolean isExcludedPath(String path) {
        return path.startsWith("/api/users/signup") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/users/refresh");  // 리프레시 엔드포인트도 예외 처리
    }

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
