package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.SeatHeart;
import com.example.SEENEMA.post.view.domain.SeatViewPost;
import com.example.SEENEMA.post.view.domain.ViewPost;
import com.example.SEENEMA.post.view.domain.ViewPostHeart;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatHeartRepository extends JpaRepository<SeatHeart, Long> {
    @Query
    List<SeatHeart> findByViewPost(SeatViewPost viewPost);
    @Query
    SeatHeart findByUserAndViewPost(User user, SeatViewPost viewPost);
    List<SeatHeart> findByUser(User user);
}
