package com.myapp.app.repository;

import com.myapp.app.entity.Like;
import com.myapp.app.entity.Post;
import com.myapp.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserAndPost(User user, Post post);
    long countByPost(Post post);
}