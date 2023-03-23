package com.example.SEENEMA.mainPage.dto;

import com.example.SEENEMA.mainPage.domain.Musical;
import lombok.*;

@Getter
@Setter
public class PlayDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalList {
        private String title;
        private String genre;
        private String date;
        private String place;
        private String cast;
        private String imgUrl;
        private String detailUrl;

        public musicalList(String imgUrl, String title, String place) {
            this.title = title;
            this.imgUrl = imgUrl;
            this.place = place;
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
}



