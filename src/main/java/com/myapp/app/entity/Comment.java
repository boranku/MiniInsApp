package com.myapp.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Post post;

    @Column(length = 1000)
    private String text;

    private OffsetDateTime createdAt = OffsetDateTime.now();
}
