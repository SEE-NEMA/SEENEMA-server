package com.example.SEENEMA.domain.seat.arcoTheater.repository;

import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoHeart;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArcoHeartRepository extends JpaRepository<ArcoHeart, Long> {
    @Query
    List<ArcoHeart> findByViewPost(ArcoPost viewPost);
    @Query
    ArcoHeart findByUserAndViewPost(User user, ArcoPost viewPost);
    List<ArcoHeart> findByUser(User user);
}
