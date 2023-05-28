package com.example.SEENEMA.domain.seat.chungmu.repository;

import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChungmuPostRepository extends JpaRepository<ChungmuPost, Long> {
    List<ChungmuPost> findByTheater_TheaterId(Long theaterId);
    ChungmuPost findByTheater_TheaterIdAndChungmuSeat_SeatIdAndViewNo(Long theaterId, Long seatId, Long viewNo);
    List<ChungmuPost> findByTheater_TheaterIdAndChungmuSeat_SeatId(Long theaterId, Long seatId);
    List<ChungmuPost> findByUser(User user);
}