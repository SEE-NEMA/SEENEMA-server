package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.ViewPost;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewPostRepository extends JpaRepository <ViewPost,Long> {
    List<ViewPost> findByTheater_TheaterId(Long theaterId);
    List<ViewPost> findByTheater_TheaterIdAndTitleContaining(Long theaterId, String seatName);
    ViewPost findByTheater_TheaterIdAndViewNo(Long theaterId, Long viewNo);
    List<ViewPost> findByUser(User user);

}
