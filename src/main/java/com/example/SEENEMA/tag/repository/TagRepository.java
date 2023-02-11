package com.example.SEENEMA.tag.repository;

import com.example.SEENEMA.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Override
    List<Tag> findAll();
}
