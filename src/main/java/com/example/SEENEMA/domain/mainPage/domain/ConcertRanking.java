package com.example.SEENEMA.domain.mainPage.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "Ranking_concert")
@NoArgsConstructor
@Entity
public class ConcertRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="ranking")
    private int ranking;      // 순위
    @Column(name = "title")
    private String title;

    @Column(name = "imgUrl")
    private String imgUrl;
    @Column(name = "upDown")
    private int upDown;     // 1 : up , 0 : down

    @Column(name="upDownRange")
    private int range;      // updown 폭?
    // (upDown, range) : (1,0) 순위변동 X
    //                 : (0,0) new
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "no")
    private Concert concert;
    @Builder
    public ConcertRanking(int id, int ranking, String title, String imgUrl, Concert concert, int upDown, int range){
        this.id = id;
        this.ranking= ranking;
        this.title = title;
        this.imgUrl = imgUrl;
        this.concert = concert;
        this.upDown = upDown;
        this.range = range;
    }
}
