package com.example.SEENEMA.domain.theater.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "THEATER_IMAGE")
@NoArgsConstructor(access= AccessLevel.PUBLIC)
@Entity
public class TheaterImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column
    private String imgUrl;
}
