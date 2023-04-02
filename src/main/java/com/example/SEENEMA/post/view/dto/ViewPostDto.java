package com.example.SEENEMA.post.view.dto;

import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.post.view.domain.ViewPost;
import lombok.*;


@Getter
@Setter
public class ViewPostDto {

    /** 게시글 등록을 처리할 Request 클래스 */
    @Getter
    @Setter
    public static class addRequest{
        private User user; // 작성자 id
        private Theater theater; // 공연장
        private String play; // 공연
        private String seat; // 좌석
        private String title; // 제목
        private String content; // 내용

        public ViewPost toEntity(){
            return ViewPost.builder()
                    .user(user)
                    .theater(theater)
                    .play(play)
                    .seat(seat)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    /** 게시글 수정을 처리할 Request 클래스 */
    @Getter
    @Setter
    public static class updateRequest {
        private String play;
        private String seat;
        private String title;
        private String content;

    }

    /** 게시글 정보를 리턴할 Response 클래스 */
    @Getter
    public static class addResponse{
        private Long userId; // 작성자 id
        private String nickName; // 작성자 닉네임
        private String theaterName; // 공연장
        private String play; // 공연
        private String seat; // 좌석
        private String title; // 제목
        private String content; // 내용
        private String createdAt;  // 작성일
        private String editedAt;

        public addResponse(ViewPost view){
            this.userId = view.getUser().getUserId();
            this.nickName = view.getUser().getNickname();
            this.theaterName = view.getTheater().getTheaterName();
            this.play = view.getPlay();
            this.seat = view.getSeat();
            this.title = view.getTitle();
            this.content = view.getContent();
            this.createdAt = view.getCreatedAt().toString();
            this.editedAt = view.getEditedAt().toString();

        }
    }

    /** 게시글 상세정보를 리턴할 Response 클래스 */
    @Getter
    public static class detailResponse{
        private Long viewNo;
        private String nickName;
        private String title;
        private String content;

        public detailResponse(ViewPost view){
            this.viewNo = view.getViewNo();
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.content = view.getContent();
        }
    }

    /** 게시글 목록을 리턴할 Response 클래스 */
    @Getter
    public static class viewListResponse{
        private String nickName;
        private String title;
        private String createdAt;

        public viewListResponse(ViewPost view){
            this.nickName = view.getUser().getNickname();
            this.title = view.getTitle();
            this.createdAt = view.getCreatedAt().toString();
        }
    }
}
