package com.example.SEENEMA.domain.seat.chungmu.repository;

import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ChungmuRepository extends JpaRepository<ChungmuSeat, Long> {
    @Query
    ChungmuSeat findByXAndYAndZ(Integer x, Integer y, Integer z);
}