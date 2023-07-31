package com.example.SEENEMA.domain.mainPage.dto;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.ConcertRanking;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.mainPage.domain.MusicalRanking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MainPageDto {

    private List<concertRanking> concertRank;
    private List<musicalRanking> musicalRank;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class concertRanking {
        private int ranking;      // 순위
        private String title;   // 공연 명
        private String imgUrl; // 이미지
        private Concert concert;

        // 생성자 추가
        public concertRanking(int ranking, String title, String imgUrl, Concert concert) {
            this.ranking = ranking;
            this.title = title;
            this.imgUrl = imgUrl;
            this.concert = concert;
        }

        // 일반 메소드
        public void setConcertRanking(ConcertRanking concertRanking) {
            this.ranking = concertRanking.getRanking();
            this.title = concertRanking.getTitle();
            this.imgUrl = concertRanking.getImgUrl();
            this.concert = concertRanking.getConcert();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalRanking{
        private int ranking;      // 순위
        private String title;   // 공연 명
        private String imgUrl; // 이미지
        private Musical musical;

        // 생성자 추가
        public musicalRanking(int ranking, String title, String imgUrl, Musical musical) {
            this.ranking = ranking;
            this.title = title;
            this.imgUrl = imgUrl;
            this.musical = musical;
        }

        // 일반 메소드
        public void setMusicalRanking(MusicalRanking musicalRanking) {
            this.ranking = musicalRanking.getRanking();
            this.title = musicalRanking.getTitle();
            this.imgUrl = musicalRanking.getImgUrl();
            this.musical = musicalRanking.getMusical();
        }
    }

}
