package com.example.SEENEMA.domain.mainPage.domain;

import com.example.SEENEMA.domain.theater.domain.Theater;
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
    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;
    @Column(name = "cast")
    private String cast;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name="interpark_url")
    private String interparkUrl;
    @Column(name="melon_url")
    private String melonUrl;
    @Column(name="eleven_url")
    private String elevenUrl;

    @Builder
    public Musical(Long no, String title, String genre, String date, String place, Theater theater, String cast, String imgUrl, String interparkUrl, String melonUrl, String elevenUrl) {
        this.no = no;
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.place = place;
        this.theater = theater;
        this.cast = cast;
        this.imgUrl = imgUrl;
        this.interparkUrl = interparkUrl;
        this.melonUrl = melonUrl;
        this.elevenUrl = elevenUrl;
    }
}