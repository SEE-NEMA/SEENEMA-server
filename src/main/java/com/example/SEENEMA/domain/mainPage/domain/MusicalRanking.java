package com.example.SEENEMA.domain.mainPage.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "Ranking_musical")
@NoArgsConstructor
@Entity
public class MusicalRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="ranking")
    private int ranking;      // 순위
    @Column(name = "title")
    private String title;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Builder
    public MusicalRanking(int id, int ranking, String title, String imgUrl){
        this.id = id;
        this.ranking= ranking;
        this.title = title;
        this.imgUrl = imgUrl;
    }
}
