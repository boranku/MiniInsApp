package com.myapp.app.controller;

import com.myapp.app.dto.PostRequest;
import com.myapp.app.dto.PostResponse;
import com.myapp.app.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest req) {
        return ResponseEntity.ok(postService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable UUID id, @Valid @RequestBody PostRequest req) {
        return ResponseEntity.ok(postService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> view(@PathVariable UUID id) {
        postService.view(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> all() {
        return ResponseEntity.ok(postService.all());
    }
}
