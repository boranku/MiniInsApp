package com.myapp.app.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID id;
    private UUID authorId;
    private String authorUsername;
    private String text;
    private OffsetDateTime createdAt;
}
