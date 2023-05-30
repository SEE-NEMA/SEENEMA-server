package com.example.SEENEMA.domain.user.repository;

import com.example.SEENEMA.domain.user.domain.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long>{

}