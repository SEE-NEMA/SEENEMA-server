package com.example.SEENEMA.user.service;

import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.dto.MyPageDto;
import org.springframework.stereotype.Service;

@Service
public interface MyPageService {
    MyPageDto.MyPageResponse loadMyPage(User user);
    String checkNickname(User user, MyPageDto.EditProfileRequest request);
    MyPageDto.MyPageResponse editProfile(User user, MyPageDto.EditProfileRequest request);
}
