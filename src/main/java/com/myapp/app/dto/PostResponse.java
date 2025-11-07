package com.myapp.app.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostResponse {
    private UUID id;
    private UUID authorId;
    private String authorUsername;
    private String description;
    private String imageUrl;
    private long views;
    private long likes;
    private List<CommentResponse> comments;
    private OffsetDateTime createdAt;
}
