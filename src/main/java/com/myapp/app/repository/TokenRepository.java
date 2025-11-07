package com.myapp.app.repository;

import com.myapp.app.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    Optional<Token> findByValueAndActiveTrue(String value);

    @Query("SELECT t FROM Token t WHERE t.value = :value AND t.active = true AND t.expiresAt > :now")
    Optional<Token> findActiveByToken(String value, OffsetDateTime now);

    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.active = true AND t.expiresAt > :now")
    List<Token> findActiveTokensByUser(UUID userId, OffsetDateTime now);

    @Modifying
    @Query("UPDATE Token t SET t.active = false WHERE t.user.id = :userId")
    void invalidateAllUserTokens(UUID userId);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(OffsetDateTime now);

    void deleteByValue(String value);
}
