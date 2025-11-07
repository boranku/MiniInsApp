package com.myapp.app.service;

import com.myapp.app.dto.AuthRequest;
import com.myapp.app.dto.AuthResponse;
import com.myapp.app.dto.SignupRequest;
import com.myapp.app.entity.Token;
import com.myapp.app.entity.User;
import com.myapp.app.exception.BadRequestException;
import com.myapp.app.repository.TokenRepository;
import com.myapp.app.repository.UserRepository;
import com.myapp.app.util.PasswordEncoder;
import com.myapp.app.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.token.expiration-hours}")
    private long tokenExpirationHours;

    @Transactional
    public void signup(SignupRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            throw new BadRequestException("Username and password are required");
        }
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(com.myapp.app.entity.Role.USER);

        userRepository.save(u);
    }

    @Transactional
    public AuthResponse login(AuthRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String tokenValue = tokenGenerator.generateToken();
        Token t = new Token();
        t.setValue(tokenValue);
        t.setUser(u);
        t.setActive(true);
        t.setExpiresAt(OffsetDateTime.now().plusHours(tokenExpirationHours));
        tokenRepository.save(t);

        return new AuthResponse(tokenValue, t.getExpiresAt().toString());
    }

    @Transactional
    public void logout(String tokenValue) {
        tokenRepository.deleteByValue(tokenValue);
    }}
