package com.example.SEENEMA.user.repository;

import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findByNickname(String email);
}
