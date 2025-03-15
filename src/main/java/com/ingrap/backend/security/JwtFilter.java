package com.ingrap.backend.security;

import com.ingrap.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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

        // ✅ 예외 처리할 엔드포인트 (로그인, 회원가입, 토큰 재발급)
        if (isExcludedPath(requestPath)) {
            logger.info("예외 엔드포인트, 필터 통과: {}"+ requestPath);
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: {}"+ authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            logger.info("요청된 토큰: " + token);

            try {
                // ✅ 블랙리스트에 등록된 토큰은 차단
                if (jwtUtil.isTokenBlacklisted(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("블랙리스트에 등록된 토큰입니다.");
                    return;
                }

                // ✅ 토큰 검증
                if (!jwtUtil.validateToken(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("유효하지 않은 토큰입니다.");
                    return;
                }

                // ✅ 리프레시 토큰이면 차단
                if (jwtUtil.isRefreshToken(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("액세스 토큰만 허용됩니다.");
                    return;
                }

                // ✅ 정상적인 액세스 토큰이라면, 사용자 인증 처리
                String email = jwtUtil.extractEmail(token);
                Claims claims = jwtUtil.getClaims(token);
                logger.info("토큰에서 추출한 이메일: {}"+ email);

                UserDetails userDetails = User.withUsername(email).password("").roles("USER").build();
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(userDetails, claims);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("인증 완료: {}" + email);

            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("토큰이 만료되었습니다.");
                return;
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("잘못된 JWT 토큰입니다.");
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("서버 오류가 발생했습니다.");
                return;
            }
        } else {
            logger.warn("Authorization Header가 없습니다.");
        }

        chain.doFilter(request, response);
    }

    // ✅ 예외 엔드포인트 관리 (회원가입, 로그인, 토큰 재발급 등)
    private boolean isExcludedPath(String path) {
        return path.startsWith("/api/users/signup") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/users/refresh");  // 리프레시 엔드포인트도 예외 처리
    }
}
