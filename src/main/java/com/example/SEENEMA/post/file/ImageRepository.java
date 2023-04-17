package com.example.SEENEMA.post.file;

import com.example.SEENEMA.post.file.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Override
    List<Image> findAll();
}
