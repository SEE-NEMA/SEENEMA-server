package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.SeatViewPost;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatViewPostRepository extends JpaRepository<SeatViewPost, Long> {
    List<SeatViewPost> findByTheater_TheaterId(Long theaterId);
    List<SeatViewPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    SeatViewPost findByTheater_TheaterIdAndViewNo(Long theaterId, Long viewNo);
    List<SeatViewPost> findByUser(User user);
}