package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.ViewPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewPostRepository extends JpaRepository <ViewPost,Long> {
}
