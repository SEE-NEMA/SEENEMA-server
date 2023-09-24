package com.example.SEENEMA.domain.user.dto;

import com.example.SEENEMA.domain.post.comment.domain.Comment;
import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardPost;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuPost;
import com.example.SEENEMA.domain.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MyPageDto {
    @Getter
    @Setter
    public static class MyPageResponse{
        private Long userId;
        private String email;
        private String nickname;
        private String interparkId;
        private String interparkPwd;
        public MyPageResponse(User user){
            this.userId = user.getUserId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
            this.interparkId = user.getInterparkId();
            this.interparkPwd = user.getInterparkPwd();
        }
    }
    @Getter
    @Setter
    public static class WithPoints{
        private Long userId;
        private String email;
        private String nickname;
        private Long points;
        public WithPoints(User user){
            this.userId = user.getUserId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
        }
    }
    @Getter
    public static class EditProfileRequest{
        private String nickname;
    }

    @Getter
    public static class InterparkLoginInfo{
        private String interparkId;
        private String interparkPwd;
    }

    // 내가 쓴 + 내가 좋아요 한 후기 목록은 기존 DTO 사용
    @Getter
    public static class MyCommentList implements Comparable<MyCommentList>{
        private Long postNo;        // 게시글 번호
        private String title;         // 게시글 제목
        private Long commentId;     // comment id
        private String nickname;    // 작성자 닉네임
        private String content;     // 내용
        private LocalDateTime createdAt;   // 생성일
        private LocalDateTime editedAt;    // 수정일
        public MyCommentList(Comment comment){
            this.postNo = comment.getTheaterPost().getPostNo();
            this.title = comment.getTheaterPost().getTitle();
            this.commentId = comment.getComment_id();
            this.nickname = comment.getUser().getNickname();
            this.content = comment.getContent();
            this.createdAt =comment.getCreatedAt();
            this.editedAt = comment.getEditedAt();
        }

        @Override
        public int compareTo(MyCommentList listDTO) {
            if(listDTO.getCommentId() < commentId) return 1;
            else if(listDTO.getCommentId() > commentId) return -1;
            return 0;
        }
    }
    @Setter
    @Getter
    public static class MySeatList extends SeatDto.seatViewList {
        private String seatName;
        private int x;
        private int y;
        private int z;
        private Long theaterId;

        public MySeatList(ArcoPost view) {
            super(view);
            this.seatName = view.getTheater().getTheaterName().toString() + "  " + view.getArcoSeat().getSeatNumber();
            this.x = view.getArcoSeat().getX();
            this.y = view.getArcoSeat().getY();
            this.z = view.getArcoSeat().getZ();
            this.theaterId = view.getTheater().getTheaterId();
        }

        public MySeatList(ShinhanPost view) {
            super(view);
            this.seatName = view.getTheater().getTheaterName().toString() + "  " + view.getShinhanSeat().getSeatNumber();
            this.x = view.getShinhanSeat().getX();
            this.y = view.getShinhanSeat().getY();
            this.z = view.getShinhanSeat().getZ();
            this.theaterId = view.getTheater().getTheaterId();
        }

        public MySeatList(ChungmuPost view) {
            super(view);
            this.seatName = view.getTheater().getTheaterName().toString() + "  " + view.getChungmuSeat().getSeatNumber();
            this.x = view.getChungmuSeat().getX();
            this.y = view.getChungmuSeat().getY();
            this.z = view.getChungmuSeat().getZ();
            this.theaterId = view.getTheater().getTheaterId();
        }

        public MySeatList(MastercardPost view) {
            super(view);
            this.seatName = view.getTheater().getTheaterName().toString() + "  " + view.getMastercardSeat().getSeatNumber();
            this.x = view.getMastercardSeat().getX();
            this.y = view.getMastercardSeat().getY();
            this.z = view.getMastercardSeat().getZ();
            this.theaterId = view.getTheater().getTheaterId();
        }
    }
}
