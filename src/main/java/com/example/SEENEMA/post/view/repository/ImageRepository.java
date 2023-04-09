package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Override
    List<Image> findAll();
}
