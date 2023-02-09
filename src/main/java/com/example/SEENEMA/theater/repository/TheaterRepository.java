package com.example.SEENEMA.theater.repository;

import com.example.SEENEMA.theater.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TheaterRepository extends JpaRepository <Theater, Long> {
    @Override
    ArrayList<Theater> findAll();
}
