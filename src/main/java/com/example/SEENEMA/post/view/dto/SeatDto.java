package com.example.SEENEMA.post.view.dto;

import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.view.domain.Seat;
import com.example.SEENEMA.post.view.domain.SeatViewPost;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeatDto{

/** 게시글 등록을 처리할 Request 클래스 */
    @Getter
    @Setter
    public static class addRequest{
        private User user; // 작성자 id
        private Theater theater; // 공연장
        private String play; // 공연
        private Seat seat; // 좌석
        private String title; // 제목
        private String content; // 내용
        private Integer viewScore;   // 시야 점수
        private Integer seatScore;   // 좌석 점수
        private Integer lightScore;  // 조명 점수
        private Integer soundScore;  // 음향 점수
        private List<Image> image; // 이미지 url

        public SeatViewPost toEntity(){
            return SeatViewPost.builder()
                    .user(user)
                    .theater(theater)
                    .play(play)
                    .seat(seat)
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

    /** 게시글 정보를 리턴할 Response 클래스 */
    @Getter
    public static class addResponse{
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

        public addResponse(SeatViewPost view){
            this.userId = view.getUser().getUserId();
            this.nickName = view.getUser().getNickname();
            this.theaterName = view.getTheater().getTheaterName();
            this.play = view.getPlay();
            this.seatName = view.getSeat().getSeatNumber();
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

    /** 게시글 상세정보를 리턴할 Response 클래스 */
    @Getter
    @Setter
    public static class detailResponse{
        private Long viewNo;
        private String nickName;
        private String title;
        private String content;
        private String seatName;
        private Integer viewScore;   // 시야 점수
        private Integer seatScore;   // 좌석 점수
        private Integer lightScore;  // 조명 점수
        private Integer soundScore;  // 음향 점수
        private List<Image> image;
        private Long heartCount;       //좋아요 갯수
        private Boolean heartedYN;      // 로그인한 사용자의 좋아요 여부

        public detailResponse(SeatViewPost view){
            this.viewNo = view.getViewNo();
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.content = view.getContent();
            this.seatName = view.getSeat().getSeatNumber();
            this.viewScore = view.getViewScore();
            this.seatScore = view.getSeatScore();
            this.lightScore = view.getLightScore();
            this.soundScore = view.getSoundScore();
            this.image = view.getImage();
            this.heartedYN = Boolean.FALSE;
            this.heartCount = view.getHeartCount();
        }
    }

    /** 게시글 목록을 리턴할 Response 클래스 */
    @Getter
    public static class seatViewList implements Comparable<seatViewList>{
        private Long viewNo;
        private String nickName;
        private String title;
        private String createdAt;
        private Long heartCount;
        private Integer viewScore;   // 시야 점수

        public seatViewList(SeatViewPost view){
            this.viewNo = view.getViewNo();
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.createdAt = view.getCreatedAt().toString();
            this.heartCount = view.getHeartCount();
            this.viewScore = view.getViewScore();
        }

        @Override
        public int compareTo(seatViewList listDTO) {
            if(listDTO.getViewNo() < viewNo) return 1;
            else if(listDTO.getViewNo() > viewNo) return -1;
            return 0;
        }
    }
}