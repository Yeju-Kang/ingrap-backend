package com.ingrap.backend.module.user.service;

import com.ingrap.backend.module.user.entity.BlacklistedToken;
import com.ingrap.backend.module.user.entity.RefreshToken;
import com.ingrap.backend.module.user.entity.User;
import com.ingrap.backend.module.user.repository.BlacklistedTokenRepository;
import com.ingrap.backend.module.user.repository.RefreshTokenRepository;
import com.ingrap.backend.module.user.repository.UserRepository;
import com.ingrap.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final long refreshTokenValidityInSeconds = 14 * 24 * 60 * 60;

    // ✅ 회원가입
    public User signupUser(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user);
    }

    // ✅ 로그인
    public Map<String, String> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // ✅ 수정된 부분
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.setToken(refreshToken);
                            existingToken.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInSeconds));
                            refreshTokenRepository.save(existingToken);
                        },
                        () -> {
                            RefreshToken newToken = RefreshToken.builder()
                                    .userId(user.getId())
                                    .token(refreshToken)
                                    .expiredAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInSeconds))
                                    .build();
                            refreshTokenRepository.save(newToken);
                        }
                );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    // ✅ 리프레시 토큰 → 새 액세스 토큰 발급
    public String refreshAccessToken(String refreshToken) {
        if (!isRefreshToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh Token Expired");
        }

        // ✅ userId로 사용자 조회하여 email도 가져옴
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return jwtUtil.generateAccessToken(user.getId(), user.getEmail());
    }

    // ✅ 로그아웃
    public void logoutUser(String accessToken, String refreshToken) {
        LocalDateTime expiredAt = jwtUtil.getExpirationDate(accessToken).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);

        if (!blacklistedTokenRepository.existsByToken(accessToken)) {
            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(accessToken)
                    .expiredAt(expiredAt)
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    public boolean isRefreshToken(String token) {
        return jwtUtil.isRefreshToken(token);
    }
}
