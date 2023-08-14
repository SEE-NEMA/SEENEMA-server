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
        private int upDown;     // 1 : up , 0 : down
        private int range;      // updown 폭?
        // (upDown, range) : (1,0) 순위변동 X
        //                 : (0,0) new
        private Concert concert;

        // 생성자 추가
        public concertRanking(int ranking, String title, String imgUrl, Concert concert, int upDown, int range) {
            this.ranking = ranking;
            this.title = title;
            this.imgUrl = imgUrl;
            this.concert = concert;
            this.upDown = upDown;
            this.range = range;
        }

        // 일반 메소드
        public void setConcertRanking(ConcertRanking concertRanking) {
            this.ranking = concertRanking.getRanking();
            this.title = concertRanking.getTitle();
            this.imgUrl = concertRanking.getImgUrl();
            this.concert = concertRanking.getConcert();
            this.upDown = concertRanking.getUpDown();
            this.range = concertRanking.getRange();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalRanking{
        private int ranking;      // 순위
        private String title;   // 공연 명
        private String imgUrl; // 이미지
        private int upDown;     // 1 : up , 0 : down
        private int range;      // updown 폭?
        // (upDown, range) : (1,0) 순위변동 X
        //                 : (0,0) new
        private Musical musical;

        // 생성자 추가
        public musicalRanking(int ranking, String title, String imgUrl, Musical musical, int upDown, int range) {
            this.ranking = ranking;
            this.title = title;
            this.imgUrl = imgUrl;
            this.musical = musical;
            this.upDown = upDown;
            this.range = range;
        }

        // 일반 메소드
        public void setMusicalRanking(MusicalRanking musicalRanking) {
            this.ranking = musicalRanking.getRanking();
            this.title = musicalRanking.getTitle();
            this.imgUrl = musicalRanking.getImgUrl();
            this.musical = musicalRanking.getMusical();
            this.upDown = musicalRanking.getUpDown();
            this.range = musicalRanking.getRange();
        }
    }

}
