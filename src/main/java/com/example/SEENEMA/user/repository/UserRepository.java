package com.example.SEENEMA.user.repository;

import com.example.SEENEMA.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
