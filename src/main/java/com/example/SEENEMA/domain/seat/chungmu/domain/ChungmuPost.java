package com.example.SEENEMA.domain.seat.chungmu.domain;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Table(name = "seat_chungmu_post")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ChungmuPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewNo; // 게시글 no

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 작성자 id

    @ManyToOne
    @JoinColumn(name="theater_id")
    private Theater theater; // 공연장

    @Column
    private String play; // 공연

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private ChungmuSeat chungmuSeat;
    @Column
    private String title;  // 제목

    @Column
    private String content; // 내용
    @Column
    private Integer viewScore;   // 시야 점수
    @Column
    private Integer seatScore;   // 좌석 점수
    @Column
    private Integer lightScore;  // 조명 점수
    @Column
    private Integer soundScore;  // 음향 점수

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> image;

    @CreatedDate // 생성일 자동화
    @Column(updatable = false)
    private LocalDateTime createdAt; // 작성 일시

    @LastModifiedDate // 수정일 자동화
    @Column
    private LocalDateTime editedAt; // 최종 수정 일시

    @Builder
    public ChungmuPost(User user, Theater theater, String play, ChungmuSeat chungmuSeat, String title, String content,
                       Integer viewScore, Integer seatScore, Integer lightScore, Integer soundScore, List<Image> image, LocalDateTime createdAt) {
        this.user = user;
        this.theater = theater;
        this.play = play;
        this.chungmuSeat = chungmuSeat;
        this.title = title;
        this.content = content;
        this.viewScore = viewScore;
        this.seatScore = seatScore;
        this.lightScore = lightScore;
        this.soundScore = soundScore;
        this.image = image;
        this.createdAt = createdAt;
    }

    public void updateSeatPost(String play,String title, String content,
                               Integer viewScore, Integer seatScore, Integer lightScore, Integer soundScore,List<Image> image) {
        this.play = play;
        this.title = title;
        this.content = content;
        this.viewScore = viewScore;
        this.seatScore = seatScore;
        this.lightScore = lightScore;
        this.soundScore = soundScore;
        this.image = image;
    }

}