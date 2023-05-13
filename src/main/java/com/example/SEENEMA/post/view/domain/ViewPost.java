package com.example.SEENEMA.post.view.domain;

import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.user.domain.User;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Table(name = "POST_VIEW")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ViewPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewNo; // 게시글 no

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 작성자 id

    @ManyToOne
    @JoinColumn(name="theater_id")
    private Theater theater; // 공연장

    @Column(nullable = false)
    private String play; // 공연

    @Column(nullable = false)
    private String seat; // 좌석

    @Column(nullable=false)
    private String title;  // 제목

    @Column(nullable = false)
    private String content; // 내용
    @Column
    private Double viewScore;   // 시야 점수
    @Column
    private Double seatScore;   // 좌석 점수
    @Column
    private Double lightScore;  // 조명 점수
    @Column
    private Double soundScore;  // 음향 점수

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> image;

    @CreatedDate // 생성일 자동화
    @Column(updatable = false)
    private LocalDateTime createdAt; // 작성 일시

    @LastModifiedDate // 수정일 자동화
    @Column
    private LocalDateTime editedAt; // 최종 수정 일시
    @Column
    private Long heartCount;

    @Builder
    public ViewPost(User user, Theater theater, String play, String seat, String title, String content,
                    Double viewScore, Double seatScore, Double lightScore, Double soundScore,List<Image> image, LocalDateTime createdAt) {
        this.user = user;
        this.theater = theater;
        this.play = play;
        this.seat = seat;
        this.title = title;
        this.content = content;
        this.viewScore = viewScore;
        this.seatScore = seatScore;
        this.lightScore = lightScore;
        this.soundScore = soundScore;
        this.image = image;
        this.createdAt = createdAt;
        this.heartCount = 0L;
    }

    public void updateViewPost(String play, String seat, String title, String content,
                               Double viewScore, Double seatScore, Double lightScore, Double soundScore,List<Image> image) {
        this.play = play;
        this.seat = seat;
        this.title = title;
        this.content = content;
        this.viewScore = viewScore;
        this.seatScore = seatScore;
        this.lightScore = lightScore;
        this.soundScore = soundScore;
        this.image = image;
    }
}