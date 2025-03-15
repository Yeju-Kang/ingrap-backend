package com.ingrap.backend.module.user.service;

import com.ingrap.backend.module.user.repository.BlacklistedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public TokenCleanupService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    // 매일 새벽 2시에 만료된 토큰 삭제
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanUpExpiredTokens() {
        blacklistedTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}
