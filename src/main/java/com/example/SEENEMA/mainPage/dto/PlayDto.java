package com.example.SEENEMA.mainPage.dto;

import com.example.SEENEMA.mainPage.domain.Musical;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class PlayDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class musicalList {
        private String musicalId;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private String place;
        private String imgUrl;


        public musicalList(Musical musical){
            this.title = musical.getTitle();
            this.imgUrl = musical.getImgUrl();
            this.startDate = LocalDate.parse(musical.getStartDate().toString());
            this.endDate = LocalDate.parse(musical.getEndDate().toString());
            this.place = musical.getPlace();
        }
    }
}



