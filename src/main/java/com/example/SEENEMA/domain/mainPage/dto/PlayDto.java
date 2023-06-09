package com.example.SEENEMA.domain.mainPage.dto;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import lombok.*;

@Getter
@Setter
public class PlayDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalList {
        private Long no;
        private String title;
        private String genre;
        private String date;
        private String place;
        private String cast;
        private String imgUrl;
        private String detailUrl;

        public musicalList(Long no, String imgUrl, String title, String place, String date, String detailUrl) {
            this.no = no;
            this.title = title;
            this.imgUrl = imgUrl;
            this.place = place;
            this.date = date;
            this.detailUrl = detailUrl;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalInfo {
        private String title;
        private String genre;
        private String date;
        private String place;
        private String cast;
        private String imgUrl;
        private String detailUrl;


        public musicalInfo(Musical musical){
            this.title = musical.getTitle();
            this.genre = musical.getGenre();
            this.date = musical.getDate();
            this.place = musical.getPlace();
            this.cast = musical.getCast();
            this.imgUrl = musical.getImgUrl();
            this.detailUrl = musical.getDetailUrl();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class concertList {
        private Long no;
        private String title;
        private String genre;
        private String date;
        private String place;
        private String cast;
        private String imgUrl;
        private String detailUrl;

        public concertList(Long no, String imgUrl, String title, String place, String date, String detailUrl) {
            this.no = no;
            this.title = title;
            this.imgUrl = imgUrl;
            this.place = place;
            this.date = date;
            this.detailUrl = detailUrl;
        }
        public Concert toEntity(){
            return Concert.builder()
                    .title(title)
                    .genre(genre)
                    .date(date)
                    .place(place)
                    .cast(cast)
                    .imgUrl(imgUrl)
                    .detailUrl(detailUrl)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class concertInfo {
        private String title;
        private String genre;
        private String date;
        private String place;
        private String cast;
        private String imgUrl;
        private String detailUrl;


        public concertInfo(Concert concert){
            this.title = concert.getTitle();
            this.genre = concert.getGenre();
            this.date = concert.getDate();
            this.place = concert.getPlace();
            this.cast = concert.getCast();
            this.imgUrl = concert.getImgUrl();
            this.detailUrl = concert.getDetailUrl();
        }
    }
}


