package com.example.SEENEMA.post.theater.domain;

import com.example.SEENEMA.tag.domain.Tag;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.user.domain.User;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Table(name = "POST_THEATER")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
public class TheaterPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_no; // 게시글 no

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 작성자 id

    @ManyToOne
    @JoinColumn(name="theater_id")
    private Theater theater; // 공연장 id
    @Column(nullable = false)
    private String title; // 제목
    @Column(nullable = false)
    private String content; // 내용

    @CreatedDate // 생성일 자동화
    @Column(updatable = false)
    private LocalDateTime createdAt; // 작성 일시

    @LastModifiedDate // 수정일 자동화
    @Column
    private LocalDateTime editedAt; // 최종 수정 일시

    @Builder
    public TheaterPost(User user, Theater theater, String title, String content, LocalDateTime createdAt) {
        this.user = user;
        this.theater = theater;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public void updateTheaterPost(Theater theater, String title, String content, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.theater = theater;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}