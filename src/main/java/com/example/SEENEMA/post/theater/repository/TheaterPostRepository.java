package com.example.SEENEMA.post.theater.repository;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterPostRepository extends JpaRepository<TheaterPost, Long> {
    List<TheaterPost> findByTags_TagId(Long tagId);
}