package com.example.SEENEMA.domain.mainPage.repository;

import com.example.SEENEMA.domain.mainPage.domain.MusicalRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicalRankingRepository extends JpaRepository<MusicalRanking, Long> {

    List<MusicalRanking> findByTitleAndImgUrl(String title, String imgUrl);
}
