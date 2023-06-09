package com.example.SEENEMA.domain.seat.blueSquareMasterCard.repository;

import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MastercardRepository extends JpaRepository<MastercardSeat, Long> {
    @Query
    MastercardSeat findByXAndYAndZ(Integer x, Integer y, Integer z);
}
