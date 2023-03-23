package com.example.SEENEMA.test.repository;

import com.example.SEENEMA.test.domain.tUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface tUserRepository extends JpaRepository<tUser, Long> {
    Optional<tUser> findByUserEmail(String email);
}
