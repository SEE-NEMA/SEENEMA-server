package com.example.SEENEMA.global.oauth.repository;

import com.example.SEENEMA.global.oauth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);// 이미 존재하는 판별
}
