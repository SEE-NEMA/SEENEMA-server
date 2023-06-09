package com.example.SEENEMA.domain.theater.repository;


import com.example.SEENEMA.domain.theater.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TheaterRepository extends JpaRepository <Theater, Long> {
    @Override
    ArrayList<Theater> findAll();
    List<Theater> findByTheaterNameContaining(String theaterName);
}

