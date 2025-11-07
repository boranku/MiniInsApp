package com.myapp.app.controller;

import com.myapp.app.dto.CommentRequest;
import com.myapp.app.dto.CommentResponse;
import com.myapp.app.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> add(@PathVariable UUID postId, @Valid @RequestBody CommentRequest req) {
        return ResponseEntity.ok(commentService.add(postId, req));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> list(@PathVariable UUID postId) {
        return ResponseEntity.ok(commentService.list(postId));
    }
}
