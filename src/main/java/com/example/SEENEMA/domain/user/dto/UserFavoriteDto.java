package com.example.SEENEMA.domain.user.dto;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserFavoriteDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class favoriteRequest {

        private String user;
        private List<Concert> concertFavorites;
        private List<Musical> musicalFavorites;

    }

    @Getter
    @Setter
    public static class favoriteResponse {
        private Long userId;
        private String nickName;
        private List<Concert> recommendedConcerts;
        private List<Musical> recommendedMusicals;

        public favoriteResponse(Long userId, String nickName, List<Concert> recommendedConcerts, List<Musical> recommendedMusicals) {
            this.userId = userId;
            this.nickName = nickName;
            this.recommendedConcerts = recommendedConcerts;;
            this.recommendedMusicals = recommendedMusicals;
        }
    }

}
