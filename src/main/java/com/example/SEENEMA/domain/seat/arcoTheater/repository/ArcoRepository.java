package com.example.SEENEMA.domain.seat.arcoTheater.repository;

import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ArcoRepository extends JpaRepository<ArcoSeat, Long> {
    @Query
    ArcoSeat findByXAndYAndZ(Integer x, Integer y, Integer z);
}