package com.myapp.app.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordEncoder {

    private static final int ITERATIONS = 10000;
    private static final int SALT_LENGTH = 16;

    public String encode(String rawPassword) {
        byte[] salt = generateSalt();
        byte[] hash = hash(rawPassword, salt, ITERATIONS);

        return Base64.getEncoder().encodeToString(salt)
                + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    public boolean matches(String rawPassword, String storedValue) {
        String[] parts = storedValue.split(":");
        if (parts.length != 2) return false;

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);

        byte[] newHash = hash(rawPassword, salt, ITERATIONS);

        // constant-time comparison (prevents timing attacks)
        if (newHash.length != storedHash.length) return false;
        int diff = 0;
        for (int i = 0; i < newHash.length; i++) {
            diff |= newHash[i] ^ storedHash[i];
        }
        return diff == 0;
    }

    private byte[] hash(String password, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] result = (password + Base64.getEncoder().encodeToString(salt))
                    .getBytes(StandardCharsets.UTF_8);

            for (int i = 0; i < iterations; i++) {
                result = digest.digest(result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
