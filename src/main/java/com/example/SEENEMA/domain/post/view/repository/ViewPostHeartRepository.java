package com.example.SEENEMA.domain.post.view.repository;

import com.example.SEENEMA.domain.post.view.domain.ViewPost;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.post.view.domain.ViewPostHeart;
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
