package com.myapp.app.service;

import com.myapp.app.entity.Token;
import com.myapp.app.entity.User;
import com.myapp.app.repository.TokenRepository;
import com.myapp.app.util.TokenGenerator;
import com.myapp.app.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final TokenGenerator tokenGenerator;

    @Value("${app.token.expiration-hours:24}") // ✅ Use config value with default
    private int tokenExpirationHours;

    @Override
    public Token createToken(User user) {
        OffsetDateTime expiresAt = OffsetDateTime.now().plusHours(tokenExpirationHours); // ✅ Use injected value
        String tokenValue = generateUniqueToken();
        Token token = new Token(tokenValue, user, expiresAt);
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Token> validateToken(String tokenValue) {
        Optional<Token> token = tokenRepository.findActiveByToken(tokenValue, OffsetDateTime.now());

        if (token.isEmpty()) {
            throw new UnauthorizedException("Invalid token");
        }

        // Keep it for additional safety
        if (token.get().getExpiresAt().isBefore(OffsetDateTime.now())) {
            token.get().invalidate();
            tokenRepository.save(token.get());
            throw new UnauthorizedException("Token expired");
        }

        return token;
    }

    @Override
    public void invalidateToken(String tokenValue) {
        tokenRepository.findByValueAndActiveTrue(tokenValue) // ✅ Simpler query
                .ifPresent(t -> {
                    t.invalidate();
                    tokenRepository.save(t);
                });
    }

    @Override
    public void invalidateAllUserTokens(User user) {
        tokenRepository.invalidateAllUserTokens(user.getId());
    }

    @Override
    public List<Token> getActiveUserTokens(User user) {
        return tokenRepository.findActiveTokensByUser(user.getId(), OffsetDateTime.now());
    }

    @Override
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(OffsetDateTime.now());
    }

    @Override
    public boolean isTokenValid(String tokenValue) {
        return tokenRepository.findActiveByToken(tokenValue, OffsetDateTime.now()).isPresent();
    }

    // 🔹 Helper Method
    private String generateUniqueToken() {
        String token;
        do {
            token = tokenGenerator.generateToken();
        } while (tokenRepository.findByValueAndActiveTrue(token).isPresent()); // ✅ Simpler check
        return token;
    }
}
