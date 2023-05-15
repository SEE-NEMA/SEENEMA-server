package com.example.SEENEMA.comment.repository;

import com.example.SEENEMA.comment.domain.Comment;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    List<Comment> findAll();
    List<Comment> findByUser(User user);
}
