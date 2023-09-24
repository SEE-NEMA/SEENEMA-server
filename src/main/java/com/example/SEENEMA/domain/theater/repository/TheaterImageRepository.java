package com.example.SEENEMA.domain.theater.repository;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.theater.domain.TheaterImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterImageRepository extends JpaRepository<TheaterImage, Long> {
    TheaterImage findByTheaterTheaterId(Long theaterId);
}

