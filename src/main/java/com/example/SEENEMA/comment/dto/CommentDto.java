package com.example.SEENEMA.comment.dto;

import com.example.SEENEMA.comment.domain.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    @Getter
    @Setter
    public static class readComment{
        private String nickname;    // 작성자 닉네임
        private String content;     // 내용
        private String createdAt;   // 생성일
        private String editedAt;    // 수정일
        public readComment(Comment comment){
            this.nickname = comment.getUser().getNickname();
            this.content = comment.getContent();;
            this.createdAt = comment.getCreatedAt().toString();
            this.editedAt = comment.getEditedAt().toString();
        }
    }
}
