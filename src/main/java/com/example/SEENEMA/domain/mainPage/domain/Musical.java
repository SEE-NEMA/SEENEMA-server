package com.example.SEENEMA.domain.mainPage.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
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

    @Column(name="interpark_url")
    private String interparkUrl;
    @Column(name="melon_url")
    private String melonUrl;
    @Column(name="wmp_url")
    private String wmpUrl;
    @Column(name="eleven_url")
    private String elevenUrl;

    @Builder
    public Musical(Long no, String title, String genre, String date, String place, String cast, String imgUrl, String interparkUrl, String melonUrl, String wmpUrl, String elevenUrl) {
        this.no = no;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.place = place;
        this.cast = cast;
        this.imgUrl = imgUrl;
        this.interparkUrl = interparkUrl;
        this.melonUrl = melonUrl;
        this.wmpUrl = wmpUrl;
        this.elevenUrl = elevenUrl;
    }
}