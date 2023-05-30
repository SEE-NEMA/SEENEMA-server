package com.example.SEENEMA.domain.post.theater.repository;

import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterPostRepository extends JpaRepository<TheaterPost, Long> {
    List<TheaterPost> findByTags_TagIdIn(List<Long> tagId);
    List<TheaterPost> findByUser(User user);
    @Modifying
    @Query("update TheaterPost t set t.viewCount = :viewCount where t.postNo = :postNo")
    int updateViewCount(@Param("viewCount") Long viewCount, @Param("postNo")Long postNo);
}