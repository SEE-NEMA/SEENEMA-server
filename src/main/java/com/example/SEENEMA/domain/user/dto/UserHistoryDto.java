package com.example.SEENEMA.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserHistoryDto {
    private Long userId;
    private Long concertId;
    private Long musicalId;
    private Long viewCount;

    @Getter
    @Setter
    public static class UserHistory {
        private Long userId;
        private Long concertId;
        private Long musicalId;
        private Long viewCount;

        public UserHistory(UserHistory user) {
            this.userId = user.getUserId();
            this.concertId = user.getConcertId();
            this.musicalId = user.getMusicalId();
            this.viewCount = user.getViewCount();
        }
    }
}
