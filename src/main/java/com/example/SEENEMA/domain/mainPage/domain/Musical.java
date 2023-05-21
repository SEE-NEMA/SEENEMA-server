package com.example.SEENEMA.domain.mainPage.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Table(name = "MUSICAL")
@NoArgsConstructor
@Entity
public class Musical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "date")
    private String date;

    @Column(name = "place")
    private String place;

    @Column(name = "cast")
    private String cast;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name="detail_url")
    private String detailUrl;

    @Builder
    public Musical(Long no, String title, String genre, String date, String place, String cast, String imgUrl, String detailUrl) {
        this.no = no;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.place = place;
        this.cast = cast;
        this.imgUrl = imgUrl;
        this.detailUrl = detailUrl;
    }
}