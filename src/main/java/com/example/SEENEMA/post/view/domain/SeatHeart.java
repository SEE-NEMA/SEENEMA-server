package com.example.SEENEMA.post.view.domain;

import com.example.SEENEMA.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "seat_heart")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class SeatHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "view_no")
    private SeatViewPost viewPost;

    @Builder
    public SeatHeart(User user, SeatViewPost viewPost){
        this.user = user;
        this.viewPost = viewPost;
    }
}