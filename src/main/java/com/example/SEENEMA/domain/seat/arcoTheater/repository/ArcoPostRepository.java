package com.example.SEENEMA.domain.seat.arcoTheater.repository;

import com.example.SEENEMA.domain.post.view.domain.ViewPost;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArcoPostRepository extends JpaRepository<ArcoPost, Long> {
    List<ArcoPost> findByTheater_TheaterId(Long theaterId);
    ArcoPost findByTheater_TheaterIdAndArcoSeat_SeatIdAndViewNo(Long theaterId, Long seatId, Long viewNo);
    List<ArcoPost> findByTheater_TheaterIdAndArcoSeat_SeatId(Long theaterId, Long seatId);
    List<ArcoPost> findByUser(User user);
}