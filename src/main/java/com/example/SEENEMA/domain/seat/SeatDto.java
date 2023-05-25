package com.example.SEENEMA.domain.seat;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.theater.domain.Theater;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeatDto {

    /** 게시글 등록을 처리할 Request 클래스 */
    @Getter
    @Setter
    public static class addRequest{
        private User user; // 작성자 id
        private Theater theater; // 공연장
        private String play; // 공연
        private String title; // 제목
        private String content; // 내용
        private Integer viewScore;   // 시야 점수
        private Integer seatScore;   // 좌석 점수
        private Integer lightScore;  // 조명 점수
        private Integer soundScore;  // 음향 점수
        private List<Image> image; // 이미지 url

        public ArcoPost toArcoPostEntity(){
            return ArcoPost.builder()
                    .user(user)
                    .theater(theater)
                    .play(play)
//                    .arcoSeat(seat)
                    .title(title)
                    .content(content)
                    .viewScore(viewScore)
                    .seatScore(seatScore)
                    .lightScore(lightScore)
                    .soundScore(soundScore)
                    .image(image)
                    .build();
        }

        public ShinhanPost toShinhanPostEntity(){
            return ShinhanPost.builder()
                    .user(user)
                    .theater(theater)
                    .play(play)
//                    .shinhanSeat(seat)
                    .title(title)
                    .content(content)
                    .viewScore(viewScore)
                    .seatScore(seatScore)
                    .lightScore(lightScore)
                    .soundScore(soundScore)
                    .image(image)
                    .build();
        }
    }
    /** 게시글 수정을 처리할 Request 클래스 */
    @Getter
    @Setter
    public static class updateRequest {
        private String play;
        private String title;
        private String content;
        private Integer viewScore;   // 시야 점수
        private Integer seatScore;   // 좌석 점수
        private Integer lightScore;  // 조명 점수
        private Integer soundScore;  // 음향 점수
        private List<Image> image;

    }

    /** 게시글 정보를 리턴할 Response 클래스 */
    @Getter
    @Setter
    public static class addResponse{
        private Long viewNo;
        private Long userId; // 작성자 id
        private String nickName; // 작성자 닉네임
        private String theaterName; // 공연장
        private String play; // 공연
        private String seatName; // 좌석
        private String title; // 제목
        private String content; // 내용
        private Integer viewScore;   // 시야 점수
        private Integer seatScore;   // 좌석 점수
        private Integer lightScore;  // 조명 점수
        private Integer soundScore;  // 음향 점수
        private Long heartCount;       //좋아요 갯수
        private Boolean heartedYN;      // 로그인한 사용자의 좋아요 여부
        private List<Image> image;
        private String createdAt;  // 작성일
        private String editedAt; // 수정일

        public addResponse(ArcoPost view){
            this.viewNo = view.getViewNo();
            this.userId = view.getUser().getUserId();
            this.nickName = view.getUser().getNickname();
            this.theaterName = view.getTheater().getTheaterName();
            this.play = view.getPlay();
            this.seatName = view.getArcoSeat().getSeatNumber();
            this.title = view.getTitle();
            this.content = view.getContent();
            this.viewScore = view.getViewScore();
            this.seatScore = view.getSeatScore();
            this.lightScore = view.getLightScore();
            this.soundScore = view.getSoundScore();
            this.heartedYN = Boolean.FALSE;
            this.heartCount = view.getHeartCount();
            this.image = new ArrayList<>(view.getImage());
            this.createdAt = view.getCreatedAt().toString();
            this.editedAt = view.getEditedAt().toString();
        }
        public addResponse(ShinhanPost view){
            this.viewNo = view.getViewNo();
            this.userId = view.getUser().getUserId();
            this.nickName = view.getUser().getNickname();
            this.theaterName = view.getTheater().getTheaterName();
            this.play = view.getPlay();
            this.seatName = view.getShinhanSeat().getSeatNumber();
            this.title = view.getTitle();
            this.content = view.getContent();
            this.viewScore = view.getViewScore();
            this.seatScore = view.getSeatScore();
            this.lightScore = view.getLightScore();
            this.soundScore = view.getSoundScore();
            this.heartedYN = Boolean.FALSE;
            this.heartCount = view.getHeartCount();
            this.image = new ArrayList<>(view.getImage());
            this.createdAt = view.getCreatedAt().toString();
            this.editedAt = view.getEditedAt().toString();
        }
    }

    /** 게시글 목록(seatViewList의 List)과 게시글들의 점수 평균, postedYN(게시글 유무)를 리턴할 Response 클래스*/
    @Getter
    @Setter
    public static class postList{
        private Integer average;
        private Boolean postedYN;
        private List<seatViewList> postingList = new ArrayList<>();
        public postList(List<seatViewList> list){
            this.postingList = list;
        }
    }

    @Getter
    @Setter
    public static class seatAverage {
        private int z;
        private int x;
        private int y;
        private Boolean postedYN;
        private Integer average;
    }


    /** 게시글 목록을 리턴할 Response 클래스 */
    @Getter
    public static class seatViewList implements Comparable<seatViewList>{
        private Long viewNo;
        private String nickName;
        private String title;
        private String createdAt;
        private Long heartCount;
        private Integer average = 0;    //평점

        public seatViewList(ArcoPost view){
            this.viewNo = view.getViewNo();
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.createdAt = view.getCreatedAt().toString();
            this.heartCount = view.getHeartCount();
            this.average = (Integer) (view.getViewScore()+view.getViewScore()+view.getLightScore()+view.getSoundScore())/4;
        }

        public seatViewList(ShinhanPost view){
            this.viewNo = view.getViewNo();
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.createdAt = view.getCreatedAt().toString();
            this.heartCount = view.getHeartCount();
        }
        @Override
        public int compareTo(seatViewList listDTO) {
            if(listDTO.getViewNo() < viewNo) return 1;
            else if(listDTO.getViewNo() > viewNo) return -1;
            return 0;
        }
    }
}