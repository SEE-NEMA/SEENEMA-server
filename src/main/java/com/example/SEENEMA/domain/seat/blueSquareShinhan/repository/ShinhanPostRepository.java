package com.example.SEENEMA.domain.seat.blueSquareShinhan.repository;

import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShinhanPostRepository extends JpaRepository<ShinhanPost, Long> {
    List<ShinhanPost> findByTheater_TheaterId(Long theaterId);
    List<ShinhanPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    ShinhanPost findByTheater_TheaterIdAndViewNo(Long theaterId, Long viewNo);
    List<ShinhanPost> findByUser(User user);
}
