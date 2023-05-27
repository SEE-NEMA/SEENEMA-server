package com.example.SEENEMA.domain.seat.blueSquareShinhan.repository;

import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanHeart;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShinhanHeartRepository extends JpaRepository<ShinhanHeart, Long> {
    @Query
    List<ShinhanHeart> findByViewPost(ShinhanPost shinhanPost);
    @Query
    ShinhanHeart findByUserAndViewPost(User user,ShinhanPost shinhanPost);
    @Query
    List<ShinhanHeart> findByUser(User user);
}