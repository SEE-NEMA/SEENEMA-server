package com.example.SEENEMA.domain.post.tag.domain;

import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table (name="tag")
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    private String tagName;
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    List<TheaterPost> theaterPosts;
}