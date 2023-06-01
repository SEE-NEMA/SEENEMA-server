package com.example.SEENEMA.domain.mainPage.repository;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long > {
    List<Concert> findByTitleAndDateAndPlace(String title, String date, String place);
    List<Concert> findByCastContaining(String cast);
    List<Concert> findByGenreContaining(String genre);
    List<Concert> findByTitleContainingIgnoreCase(String title);
    List<Concert> findByDetailUrl(String detailUrl);
}
