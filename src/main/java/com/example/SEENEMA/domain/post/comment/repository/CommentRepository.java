package com.example.SEENEMA.domain.post.comment.repository;

import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.post.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    List<Comment> findAll();
    List<Comment> findByUser(User user);
    @Query
    List<Comment> findByTheaterPost(TheaterPost theaterPost);
}
