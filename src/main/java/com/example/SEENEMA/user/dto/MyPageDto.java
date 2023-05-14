package com.example.SEENEMA.user.dto;

import com.example.SEENEMA.comment.domain.Comment;
import com.example.SEENEMA.user.domain.User;
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
        public MyPageResponse(User user){
            this.userId = user.getUserId();
            this.email = user.getEmail();
            this.nickname = user.getNickname();
        }
    }
    @Getter
    public static class EditProfileRequest{
        private String nickname;
    }
    // 내가 쓴 + 내가 좋아요 한 후기 목록은 기존 DTO 사용
    public static class MyCommentList{
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
    }
}
