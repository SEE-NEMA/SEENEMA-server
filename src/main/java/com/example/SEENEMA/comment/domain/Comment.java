package com.example.SEENEMA.comment.domain;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table(name="comment")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;
    @Column
    private String content;
    @Column
    private String createdBy;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime editedAt;
    @ManyToOne
    @JoinColumn(name = "post_no")
    private TheaterPost theaterPost;
}
