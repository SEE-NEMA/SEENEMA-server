package com.example.SEENEMA.domain.user.service;

import com.example.SEENEMA.domain.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.domain.post.view.dto.ViewPostDto;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.dto.MyPageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MyPageService {
    MyPageDto.MyPageResponse loadMyPage(User user);
    MyPageDto.WithPoints loadMyPageWithPoints(User user);
    String checkNickname(User user, MyPageDto.EditProfileRequest request);
    MyPageDto.MyPageResponse editProfile(User user, MyPageDto.EditProfileRequest request);
    List<TheaterPostDto.listResponse> listMyTheaterReview(User user);
    List<MyPageDto.MyCommentList> listMyComment(User user);
    List<ViewPostDto.viewListResponse> listMyViewReview(User user);
    List<TheaterPostDto.listResponse> listHeartedTheaterReview(User user);
    List<ViewPostDto.viewListResponse> listHeartedViewReview(User user);
    List<MyPageDto.MySeatList> listMySeatReview(User user);
}
