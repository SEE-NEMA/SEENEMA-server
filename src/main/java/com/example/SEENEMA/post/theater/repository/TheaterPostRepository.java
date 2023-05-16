package com.example.SEENEMA.post.theater.repository;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterPostRepository extends JpaRepository<TheaterPost, Long> {
    List<TheaterPost> findByTags_TagIdIn(List<Long> tagId);
    List<TheaterPost> findByUser(User user);
}