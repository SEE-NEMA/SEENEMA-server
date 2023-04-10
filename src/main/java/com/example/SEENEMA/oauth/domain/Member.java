package com.example.SEENEMA.oauth.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@DynamicUpdate
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = true, unique = true)
    private String nickname;

    @Builder
    public Member(Long memberId, String name, String email, String provider, String nickname){
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.nickname = nickname;
    }

    public Member update(String name, String email){
        this.name = name;
        this.email = email;
        return this;
    }
}
