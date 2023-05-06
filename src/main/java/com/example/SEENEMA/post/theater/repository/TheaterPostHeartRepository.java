package com.example.SEENEMA.post.theater.repository;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.post.theater.domain.TheaterPostHeart;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterPostHeartRepository extends JpaRepository<TheaterPostHeart, Long> {
    @Query
    TheaterPostHeart findByUserAndTheaterPost(User user, TheaterPost theaterPost);
}
