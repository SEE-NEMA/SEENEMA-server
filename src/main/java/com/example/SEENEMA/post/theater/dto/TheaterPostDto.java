package com.example.SEENEMA.post.theater.dto;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.tag.domain.Tag;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.theater.domain.Theater;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class TheaterPostDto {
    @Getter
    @Setter
    public static class addRequest{
        private User user; // 작성자 id
        private Theater theater; // 공연장
        private String title; // 제목
        private String content; // 내용
        private List<Tag> tags;

        public TheaterPost toEntity(){
            return TheaterPost.builder()
                    .user(user)
                    .theater(theater)
                    .title(title)
                    .content(content)
                    .tags(tags)
                    .build();
        }
    }
    @Getter
    public static class addResponse{
        private Long userId;
        private String nickName;
        private String theaterName;
        private String title;
        private String content;
        private String createdAt;

        private List<Tag> tags;

        public addResponse(TheaterPost theaterPost){
            this.userId=theaterPost.getUser().getUserId();
            this.nickName=theaterPost.getUser().getNickname();
            this.theaterName=theaterPost.getTheater().getTheaterName();
            this.title=theaterPost.getTitle();
            this.content=theaterPost.getContent();
            this.createdAt=theaterPost.getCreatedAt().toString();
            this.tags=theaterPost.getTags();
        }
    }
}