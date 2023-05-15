package com.example.SEENEMA.post.view.repository;

import com.example.SEENEMA.post.view.domain.ViewPost;
import com.example.SEENEMA.post.view.domain.ViewPostHeart;
import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewPostHeartRepository extends JpaRepository<ViewPostHeart, Long> {
    @Query
    List<ViewPostHeart> findByViewPost(ViewPost viewPost);
    @Query
    ViewPostHeart findByUserAndViewPost(User user, ViewPost viewPost);
    List<ViewPostHeart> findByUser(User user);
}
