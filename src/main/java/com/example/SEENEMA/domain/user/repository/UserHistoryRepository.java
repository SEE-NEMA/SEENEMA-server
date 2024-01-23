package com.example.SEENEMA.domain.user.repository;

import com.example.SEENEMA.domain.user.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    @Query("SELECT uh.concert.id FROM UserHistory uh WHERE uh.user.id = :userId ORDER BY uh.viewCount DESC, uh.timestamp DESC")
    List<Long> findMostViewedConcertIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT uh.musical.id FROM UserHistory uh WHERE uh.user.id = :userId ORDER BY uh.viewCount DESC, uh.timestamp DESC")
    List<Long> findMostViewedMusicalIdsByUserId(@Param("userId") Long userId);

    // 사용자가 조회한 공연 중 공연 ID를 가져오기 위해 user_id와 concert_id를 사용
    UserHistory findByUserUserIdAndConcertNo(Long userId, Long concertId);
    UserHistory findByUserUserIdAndMusicalNo(Long userId, Long musicalId);

}

