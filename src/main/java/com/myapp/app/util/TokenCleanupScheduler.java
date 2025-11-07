package com.myapp.app.util;

import com.myapp.app.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final TokenService tokenService;

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanUpExpiredTokens() {
        tokenService.cleanupExpiredTokens();
    }
}