package com.example.SEENEMA.mainPage.repository;

import com.example.SEENEMA.mainPage.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long > {
    List<Concert> findByTitleAndDateAndPlace(String title, String date, String place);
}
