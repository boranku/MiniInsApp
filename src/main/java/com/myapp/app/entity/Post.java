package com.myapp.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User author;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    private long views = 0;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
