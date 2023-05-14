package com.example.SEENEMA.user.dto;

public class MyPageDto {
    public static class MyPageResponse{
        private String userId;
        private String nickname;
    }
    public static class EditProfileRequest{
        private String userId;
        private String nickname;
    }
    public static class EditProfileResponse{
        private String userId;
        private String nickname;
        private String email;   // 가입한 ID
    }
    // 내가 쓴 + 내가 좋아요 한 후기 목록은 기존 DTO 사용
    public static class MyCommentList{
        private Long postNo;        // 게시글 번호
        private Long title;         // 게시글 제목
        private Long commentId;     // comment id
        private String nickname;    // 작성자 닉네임
        private String content;     // 내용
        private String createdAt;   // 생성일
        private String editedAt;    // 수정일
    }
}
