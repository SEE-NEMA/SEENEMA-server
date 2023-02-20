package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.ViewPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewPostRepository extends JpaRepository <ViewPost,Long> {
    List<ViewPost> findByTheater_TheaterId(Long theaterId);
    List<ViewPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    ViewPost findByTheater_TheaterIdAndTitleAndViewNo(Long theaterId, String title, Long viewNo);
}
