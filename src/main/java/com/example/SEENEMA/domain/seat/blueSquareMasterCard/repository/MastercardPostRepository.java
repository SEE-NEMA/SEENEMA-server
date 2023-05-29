package com.example.SEENEMA.domain.seat.blueSquareMasterCard.repository;

import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MastercardPostRepository extends JpaRepository<MastercardPost, Long> {
    List<MastercardPost> findByTheater_TheaterId(Long theaterId);
    List<MastercardPost> findByTheater_TheaterIdAndMastercardSeat_SeatId(Long theaterId, Long seatId);
    MastercardPost findByViewNo(Long viewNo);
}
