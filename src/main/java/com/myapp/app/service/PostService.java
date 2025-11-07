package com.myapp.app.service;

import com.myapp.app.dto.CommentResponse;
import com.myapp.app.dto.PostRequest;
import com.myapp.app.dto.PostResponse;
import com.myapp.app.entity.Comment;
import com.myapp.app.entity.Post;
import com.myapp.app.entity.Role;
import com.myapp.app.entity.User;
import com.myapp.app.exception.NotFoundException;
import com.myapp.app.exception.UnauthorizedException;
import com.myapp.app.repository.CommentRepository;
import com.myapp.app.repository.LikeRepository;
import com.myapp.app.repository.PostRepository;
import com.myapp.app.util.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public PostResponse create(PostRequest req) {
        User u = RequestContext.getUser();
        Post p = new Post();
        p.setAuthor(u);
        p.setDescription(req.getDescription());
        p.setImageUrl(req.getImageUrl());
        Post saved = postRepository.save(p);
        return map(saved);
    }

    public PostResponse get(UUID id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        return map(p);
    }

    public List<PostResponse> all() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(this::map).collect(Collectors.toList());
    }

    public PostResponse update(UUID id, PostRequest req) {
        Post p = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        User u = RequestContext.getUser();
        if (!p.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Not allowed");
        }
        p.setDescription(req.getDescription());
        p.setImageUrl(req.getImageUrl());
        return map(postRepository.save(p));
    }

    public void delete(UUID id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        User u = RequestContext.getUser();
        if (!p.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Not allowed");
        }
        postRepository.delete(p);
    }

    @Transactional
    public void view(UUID id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post not found");
        }
        postRepository.incrementViews(id);
    }


    private PostResponse map(Post p) {
        PostResponse r = new PostResponse();
        r.setId(p.getId());
        r.setAuthorId(p.getAuthor().getId());
        r.setAuthorUsername(p.getAuthor().getUsername());
        r.setDescription(p.getDescription());
        r.setImageUrl(p.getImageUrl());
        r.setViews(p.getViews());
        r.setLikes(likeRepository.countByPost(p));
        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtAsc(p);
        r.setComments(comments.stream().map(c -> {
            CommentResponse cr = new CommentResponse();
            cr.setId(c.getId());
            cr.setAuthorId(c.getAuthor().getId());
            cr.setAuthorUsername(c.getAuthor().getUsername());
            cr.setCreatedAt(c.getCreatedAt());
            cr.setText(c.getText());
            return cr;
        }).collect(Collectors.toList()));
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}
