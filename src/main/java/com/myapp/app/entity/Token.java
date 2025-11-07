package com.myapp.app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private OffsetDateTime expiresAt;

    private boolean active = true;

    public Token(String value, User user, OffsetDateTime expiresAt) {
        this.value = value;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(OffsetDateTime.now());
    }

    public void invalidate() {
        this.active = false;
    }
}
