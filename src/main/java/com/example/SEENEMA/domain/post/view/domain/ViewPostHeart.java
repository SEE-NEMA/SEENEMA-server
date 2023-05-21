package com.example.SEENEMA.domain.post.view.domain;

import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "POST_VIEW_HEART")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ViewPostHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "view_no")
    private ViewPost viewPost;

    @Builder
    public ViewPostHeart(User user, ViewPost viewPost){
        this.user = user;
        this.viewPost = viewPost;
    }
}
