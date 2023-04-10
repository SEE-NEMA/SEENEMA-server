package com.example.SEENEMA.oauth.repository;

import com.example.SEENEMA.oauth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);// 이미 존재하는 판별
}
