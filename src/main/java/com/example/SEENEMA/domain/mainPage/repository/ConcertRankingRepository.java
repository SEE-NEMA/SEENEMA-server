package com.example.SEENEMA.domain.mainPage.repository;

import com.example.SEENEMA.domain.mainPage.domain.ConcertRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertRankingRepository extends JpaRepository<ConcertRanking, Long> {

    List<ConcertRanking> findByTitleAndImgUrl(String title, String imgUrl);
}
