package com.myapp.app.service;

import com.myapp.app.entity.Like;
import com.myapp.app.entity.Post;
import com.myapp.app.entity.User;
import com.myapp.app.exception.NotFoundException;
import com.myapp.app.repository.LikeRepository;
import com.myapp.app.repository.PostRepository;
import com.myapp.app.util.RequestContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public void like(UUID postId) {
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        User u = RequestContext.getUser();

        if (likeRepository.findByUserAndPost(u, p).isPresent()) return;
        likeRepository.save(new Like(null, u, p));
    }

    public void unlike(UUID postId) {
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        User u = RequestContext.getUser();

        likeRepository.findByUserAndPost(u, p)
                .ifPresent(likeRepository::delete);
    }
}
