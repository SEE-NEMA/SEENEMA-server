package com.example.SEENEMA.domain.theater.dto;


import com.example.SEENEMA.domain.theater.domain.Theater;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterDto {

    /** 검색한 공연장을 리턴할 Response 클래스 */
    @Getter
    public static class theaterResponse{
        private Long theaterId;
        private String theaterName;

        public theaterResponse(Theater theater){
            this.theaterId = theater.getTheaterId();
            this.theaterName = theater.getTheaterName();
        }
    }

    @Getter
    @Setter
    public static class theaterList{
        private Long theaterId;
        private String theaterName;
        private String detailUrl;
        private Double lat;
        private Double lon;
        private String direction;
        private String parking;
        private String info;
        private String imgUrl; // 이미지 url

        public theaterList(Theater theater) {
            this.theaterId = theater.getTheaterId();
            this.theaterName = theater.getTheaterName();
            this.detailUrl = theater.getDetailUrl();
            this.lat = theater.getLat();
            this.lon = theater.getLon();
            this.direction = theater.getDirection();
            this.parking = theater.getParking();
            this.info = theater.getInfo();
            this.imgUrl = String.valueOf(theater.getImage());
        }
    }

    /** 좌석 페이지에서 검색한 공연장을 리턴할 Response 클래스 */
    @Getter
    public static class seatTheaterResponse{
        private Long theaterId;
        private String theaterName;

        public seatTheaterResponse(Theater theater){
            this.theaterId = theater.getTheaterId();
            this.theaterName = theater.getTheaterName();
        }
    }
}
