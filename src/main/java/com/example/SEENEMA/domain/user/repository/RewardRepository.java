package com.example.SEENEMA.domain.user.repository;

import com.example.SEENEMA.domain.user.domain.Reward;
import com.example.SEENEMA.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    @Query
    Reward findByUser(User user);
    List<Reward> findAll();
}
