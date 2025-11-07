package com.myapp.app.service;

import com.myapp.app.dto.CommentRequest;
import com.myapp.app.dto.CommentResponse;
import com.myapp.app.entity.Comment;
import com.myapp.app.entity.Post;
import com.myapp.app.entity.Role;
import com.myapp.app.entity.User;
import com.myapp.app.exception.NotFoundException;
import com.myapp.app.exception.UnauthorizedException;
import com.myapp.app.repository.CommentRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse add(UUID postId, CommentRequest req) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        User u = RequestContext.getUser();
        Comment c = new Comment();
        c.setAuthor(u);
        c.setPost(p);
        c.setText(req.getText());
        Comment saved = commentRepository.save(c);
        return map(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> list(UUID postId) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        return commentRepository.findByPostOrderByCreatedAtAsc(p).stream().map(this::map).collect(Collectors.toList());
    }

    public void delete(UUID commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        User u = RequestContext.getUser();

        boolean isCommentOwner = c.getAuthor().getId().equals(u.getId());
        boolean isPostOwner = c.getPost().getAuthor().getId().equals(u.getId());
        boolean isAdmin = u.getRole() == Role.ADMIN;

        if (!isCommentOwner && !isPostOwner && !isAdmin) {
            throw new UnauthorizedException("Not allowed");
        }

        commentRepository.delete(c);
    }

    private CommentResponse map(Comment c) {
        CommentResponse r = new CommentResponse();
        r.setId(c.getId());
        r.setAuthorId(c.getAuthor().getId());
        r.setAuthorUsername(c.getAuthor().getUsername());
        r.setText(c.getText());
        r.setCreatedAt(c.getCreatedAt());
        return r;
    }
}
