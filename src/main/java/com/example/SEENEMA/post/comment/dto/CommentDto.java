package com.example.SEENEMA.post.comment.dto;

import com.example.SEENEMA.post.comment.domain.Comment;
import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    @Getter
    @Setter
    public static class readComment{// 댓글 보기
        private Long commentId;     // comment id
        private String nickname;    // 작성자 닉네임
        private String content;     // 내용
        private String createdAt;   // 생성일
        private String editedAt;    // 수정일
        public readComment(Comment comment){
            this.commentId = comment.getComment_id();
            this.nickname = comment.getUser().getNickname();
            this.content = comment.getContent();;
            this.createdAt = comment.getCreatedAt().toString();
            this.editedAt = comment.getEditedAt().toString();
        }
    }
    @Getter
    @Setter
    public static class addRequest{ // 댓글 작성
        private User user;          // 작성자 id
        private TheaterPost theaterPost;    // 대상 게시글
        private String content;     // 내용
        public Comment toEntity(){
            return Comment.builder()
                    .user(user)
                    .theaterPost(theaterPost)
                    .content(content)
                    .build();
        }
    }
    public static class deleteResponse{ // 댓글 삭제
    }
}
