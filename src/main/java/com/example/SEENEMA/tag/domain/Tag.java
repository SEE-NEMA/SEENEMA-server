package com.example.SEENEMA.tag.domain;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Set;


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

    @ManyToMany(mappedBy = "tags")
    List<TheaterPost> tag;
}