package com.example.SEENEMA.theater.repository;

import com.example.SEENEMA.theater.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository <Theater, Long> {
}
