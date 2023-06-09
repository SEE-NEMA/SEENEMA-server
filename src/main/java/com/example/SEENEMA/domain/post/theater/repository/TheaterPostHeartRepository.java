package com.example.SEENEMA.domain.post.theater.repository;

import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.post.theater.domain.TheaterPostHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterPostHeartRepository extends JpaRepository<TheaterPostHeart, Long> {
    @Query
    TheaterPostHeart findByUserAndTheaterPost(User user, TheaterPost theaterPost);
    @Query
    List<TheaterPostHeart> findByTheaterPost(TheaterPost theaterPost);
    List<TheaterPostHeart> findByUser(User user);
}
