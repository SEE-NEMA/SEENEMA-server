package com.example.SEENEMA.domain.seat.blueSquareShinhan.repository;

import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShinhanRepository extends JpaRepository<ShinhanSeat, Long> {
    @Query
    ShinhanSeat findByXAndYAndZ(Integer x, Integer y, Integer z);
}
