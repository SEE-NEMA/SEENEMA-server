package com.example.SEENEMA.domain.seat.arcoTheater.domain;

import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "seat_heart")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArcoHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "view_no")
    private ArcoPost seatPost;

    @Builder
    public ArcoHeart(User user, ArcoPost seatPost){
        this.user = user;
        this.seatPost = seatPost;
    }
}