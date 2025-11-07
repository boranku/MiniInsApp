package com.myapp.app.service;

import com.myapp.app.entity.Token;
import com.myapp.app.entity.User;
import java.util.List;
import java.util.Optional;

public interface TokenService {
    Token createToken(User user);
    Optional<Token> validateToken(String tokenValue);
    void invalidateToken(String tokenValue);
    void invalidateAllUserTokens(User user);
    List<Token> getActiveUserTokens(User user);
    void cleanupExpiredTokens();
    boolean isTokenValid(String tokenValue);
}
