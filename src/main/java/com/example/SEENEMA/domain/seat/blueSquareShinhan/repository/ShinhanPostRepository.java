package com.example.SEENEMA.domain.seat.blueSquareShinhan.repository;

import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShinhanPostRepository extends JpaRepository<ShinhanPost, Long> {
    List<ShinhanPost> findByTheater_TheaterId(Long theaterId);
    List<ShinhanPost> findByTheater_TheaterIdAndShinhanSeat_SeatId(Long theaterId, Long seatId);
    List<ShinhanPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    ShinhanPost findByTheater_TheaterIdAndViewNo(Long theaterId, Long viewNo);
    List<ShinhanPost> findByUser(User user);
    Optional<ShinhanPost> findById(Long viewNo);
    List<ShinhanPost> findByShinhanSeat_SeatId(Long seatId);
}