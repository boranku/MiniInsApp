package com.myapp.app.controller;

import com.myapp.app.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable UUID postId) {
        likeService.like(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable UUID postId) {
        likeService.unlike(postId);
        return ResponseEntity.noContent().build();
    }
}
