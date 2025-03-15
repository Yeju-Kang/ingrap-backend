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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final long refreshTokenValidityInSeconds = 14 * 24 * 60 * 60; // 리프레시 토큰 유효기간 (14일)

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

    // ✅ 로그인 (액세스 토큰 & 리프레시 토큰 발급)
    public Map<String, String> loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // ✅ 리프레시 토큰 저장 (있으면 갱신, 없으면 새로 저장)
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.setToken(refreshToken);
                            existingToken.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInSeconds));
                            refreshTokenRepository.save(existingToken);
                        },
                        () -> {
                            RefreshToken newToken = RefreshToken.builder()
                                    .userId(user.getId())  // ✅ Long 타입으로 통일
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

    // ✅ 리프레시 토큰으로 액세스 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
        // 리프레시 토큰인지 검증
        if (!isRefreshToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh Token Expired");
        }

        // ✅ Long 타입 userId로 generateAccessToken 호출
        return jwtUtil.generateAccessToken(token.getUserId().toString());
    }

    // ✅ 로그아웃 처리 (리프레시 토큰 삭제 + 액세스 토큰 블랙리스트 등록)
    public void logoutUser(String accessToken, String refreshToken) {
        // ✅ expiredAt을 자동으로 계산
        LocalDateTime expiredAt = jwtUtil.getExpirationDate(accessToken).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // ✅ 리프레시 토큰 삭제
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);

        // ✅ 액세스 토큰을 블랙리스트에 등록
        if (!blacklistedTokenRepository.existsByToken(accessToken)) {
            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(accessToken)
                    .expiredAt(expiredAt)
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    // ✅ 블랙리스트 확인
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    // ✅ 리프레시 토큰인지 검증
    public boolean isRefreshToken(String token) {
        return jwtUtil.isRefreshToken(token);
    }
}
