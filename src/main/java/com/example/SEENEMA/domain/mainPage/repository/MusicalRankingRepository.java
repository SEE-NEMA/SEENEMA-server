package com.example.SEENEMA.domain.mainPage.repository;

import com.example.SEENEMA.domain.mainPage.domain.ConcertRanking;
import com.example.SEENEMA.domain.mainPage.domain.MusicalRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicalRankingRepository extends JpaRepository<MusicalRanking, Long> {
}
