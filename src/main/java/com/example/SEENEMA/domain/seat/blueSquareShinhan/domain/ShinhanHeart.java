package com.example.SEENEMA.domain.seat.blueSquareShinhan.domain;

import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "shinhan_heart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ShinhanHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "view_no")
    private ShinhanPost viewPost;

    @Builder
    public ShinhanHeart(User user, ShinhanPost viewPost){
        this.user = user;
        this.viewPost = viewPost;
    }
}
