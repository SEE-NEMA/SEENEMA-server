package com.example.SEENEMA.mainPage.domain;

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

    @Column(name = "musical_id")
    private String musicalId;
    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date", columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "place")
    private String place;

    @Builder
    public Musical(Long no, String musicalId, String imgUrl, String title, LocalDate startDate, LocalDate endDate, String place) {
        this.no = no;
        this.musicalId = musicalId;
        this.imgUrl = imgUrl;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
    }
}