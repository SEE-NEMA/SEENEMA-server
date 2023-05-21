package com.example.SEENEMA.post.theater.dto;

import com.example.SEENEMA.post.comment.dto.CommentDto;
import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.tag.domain.Tag;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.theater.domain.Theater;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TheaterPostDto {
    @Getter
    @Setter
    public static class addRequest{ // 게시글 등록 요청
        private User user;          // 작성자 id
        private Theater theater;    // 공연장
        private String title;       // 제목
        private String content;     // 내용
        private List<Tag> tags;     // 태그
        private List<Image> image; // 이미지 url

        public TheaterPost toEntity(){
            return TheaterPost.builder()
                    .user(user)
                    .theater(theater)
                    .title(title)
                    .content(content)
                    .tags(tags)
                    .image(image)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class addResponse{ // 게시글 등록 결과
        private Long userId;        // 작성자 id
        private String nickName;    // 작성자 닉네임
        private String theaterName; // 공연장 이름
        private String title;       // 게시글 제목
        private String content;     // 게시글 내용
        private String createdAt;   // 게시글 생성일
        private String editedAt;    // 게시글 수정일
        private List<Tag> tags;     // 게시글 태그
        private List<Image> image;
        private Long viewCount;    // 조회수
        private Boolean heartedYN;        // 로그인한 사용자의 좋아요 여부
        private Long heartCount;          // 좋아요 갯수
        private List<CommentDto.readComment> comments; // 댓글 목록


        public addResponse(TheaterPost theaterPost){
            this.userId = theaterPost.getUser().getUserId();
            this.nickName = theaterPost.getUser().getNickname();
            this.theaterName = theaterPost.getTheater().getTheaterName();
            this.title = theaterPost.getTitle();
            this.content = theaterPost.getContent();
            this.createdAt = theaterPost.getCreatedAt().toString();
            this.editedAt = theaterPost.getEditedAt().toString();
            this.tags = theaterPost.getTags();
            this.viewCount = theaterPost.getViewCount();
            this.heartedYN = Boolean.FALSE;
            this.heartCount = theaterPost.getHeartCount();
            this.image = new ArrayList<>(theaterPost.getImage());
        }
    }
    @Getter
    public static class listResponse implements Comparable<listResponse>{ // 공연장 후기 페이지 결과
        private Long post_no;               // 게시글 번호
        private String title;               // 게시글 제목
        private LocalDateTime createdAt;    // 게시글 생성일
        private String nickname;            // 작성자 닉네임
        private Long heartCount;            // 좋아요 갯수
        private List<Tag> tags;  // 게시글 태그

        public listResponse(TheaterPost theaterPost){
            this.post_no = theaterPost.getPostNo();
            this.title = theaterPost.getTitle();
            this.createdAt = theaterPost.getCreatedAt();
            this.nickname = theaterPost.getUser().getNickname();
            this.heartCount = theaterPost.getHeartCount();
            this.tags = new ArrayList<>(theaterPost.getTags());
        }
        @Override
        public int compareTo(listResponse listDTO){
            if(listDTO.getPost_no() < post_no) return 1;
            else if(listDTO.getPost_no() > post_no) return -1;
            return 0;
        }
    }
    @Getter
    public static class deleteResponse{ // 공연장 후기 삭제
        private String result;
        public deleteResponse(String result){ this.result = result;}
    }
}
