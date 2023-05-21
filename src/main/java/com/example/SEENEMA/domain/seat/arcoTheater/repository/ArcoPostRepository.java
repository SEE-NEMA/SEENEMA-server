package com.example.SEENEMA.domain.seat.arcoTheater.repository;

import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArcoPostRepository extends JpaRepository<ArcoPost, Long> {
    List<ArcoPost> findByTheater_TheaterId(Long theaterId);
    List<ArcoPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    ArcoPost findByTheater_TheaterIdAndViewNo(Long theaterId, Long viewNo);
    List<ArcoPost> findByUser(User user);
}