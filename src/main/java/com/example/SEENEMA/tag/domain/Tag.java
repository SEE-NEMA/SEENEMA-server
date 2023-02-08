package com.example.SEENEMA.tag.domain;

import lombok.*;
import javax.persistence.*;


@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table (name="tag")
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    @Column(nullable = false)
    private String tag_name;

}