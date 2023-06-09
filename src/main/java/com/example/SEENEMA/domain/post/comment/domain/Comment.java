package com.example.SEENEMA.domain.post.comment.domain;

import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.example.SEENEMA.domain.user.domain.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table(name="comment")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 작성자 id
    @ManyToOne
    @JoinColumn(name = "post_no")
    private TheaterPost theaterPost;
    @Column
    private String content; // 내용
    @CreatedDate // 생성일 자동화
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate // 수정일 자동화
    @Column
    private LocalDateTime editedAt;

    @Builder
    public Comment(User user,TheaterPost theaterPost, String content){
        this.user = user;
        this.theaterPost = theaterPost;
        this.content = content;
    }
}
