package com.example.SEENEMA.domain.mainPage.dto;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.ConcertRanking;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.mainPage.domain.MusicalRanking;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MainPageDto {

    private List<concertRanking> concertRank;
    private List<musicalRanking> musicalRank;

    @Getter
    @Setter
    public static class concertRanking {
        private int ranking;      // 순위
        private String title;   // 공연 명
        private String imgUrl; // 이미지
        private Concert concert;

        public void concertRanking(ConcertRanking concertRanking) {
            this.ranking = concertRanking.getRanking();
            this.title = concertRanking.getTitle();
            this.imgUrl = concertRanking.getImgUrl();
        }
    }
    @Getter
    @Setter
    public static class musicalRanking{
        private int ranking;      // 순위
        private String title;   // 공연 명
        private String imgUrl; // 이미지
        private Musical musical;

        public void musicalRanking(MusicalRanking musicalRanking){
            this.ranking = musicalRanking.getRanking();
            this.title = musicalRanking.getTitle();
            this.imgUrl = musicalRanking.getImgUrl();
        }
    }

}
