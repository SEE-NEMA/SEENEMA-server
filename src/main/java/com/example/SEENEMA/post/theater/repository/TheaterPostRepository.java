package com.example.SEENEMA.post.theater.repository;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterPostRepository extends JpaRepository<TheaterPost, Long> {
}