package com.example.SEENEMA.mainPage.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MainPageDto {
    @Getter
    @Setter
    public static class readRanking{
        private int rank;      // 순위
        private String title;   // 공연 명
    }
}
