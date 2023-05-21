package com.example.SEENEMA.domain.post.theater.domain;

import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "POST_THEATER_HEART")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class TheaterPostHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private TheaterPost theaterPost;

    @Builder
    public TheaterPostHeart(User user, TheaterPost theaterPost){
        this.user = user;
        this.theaterPost = theaterPost;
    }
}
