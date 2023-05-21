package com.example.SEENEMA.domain.theater.domain;

import lombok.*;
import javax.persistence.*;


@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table (name="theater")
@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    @Column(nullable = false)
    private String theaterName;

    @Column(name="detail_url")
    private String detailUrl;


    @Builder
    public Theater(Long theaterId, String theaterName, String detailUrl) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.detailUrl = detailUrl;
    }
}