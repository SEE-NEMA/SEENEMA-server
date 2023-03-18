package com.example.SEENEMA.mainPage.repository;

import com.example.SEENEMA.mainPage.domain.Musical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicalRepository extends JpaRepository<Musical, Long> {
    List<Musical> findByMusicalId(String musicalId);
}
