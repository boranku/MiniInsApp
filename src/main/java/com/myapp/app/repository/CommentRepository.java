package com.myapp.app.repository;

import com.myapp.app.entity.Comment;
import com.myapp.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
