package com.example.SEENEMA.domain.mainPage.repository;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long > {
    List<Concert> findByDateAndPlaceContaining(String date, String place);
    List<Concert> findByTitleAndDateAndPlace(String title, String date, String place);
    List<Concert> findByGenreOrCast(String genre, String cast);
    List<Concert> findByCast(String cast);

    Concert findByNo(Long no);

}
