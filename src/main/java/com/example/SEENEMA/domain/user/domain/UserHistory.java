package com.example.SEENEMA.domain.user.domain;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE) // CascadeType.ALL도 사용 가능
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE) // CascadeType.ALL도 사용 가능
    @JoinColumn(name = "musical_id")
    private Musical musical;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "view_count")
    private Integer viewCount; // 조회 횟수
    // getter와 setter, 생성자 등 필요한 메서드들
}
