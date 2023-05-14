package com.example.SEENEMA.user.service;

import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.dto.MyPageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyPageServiceImpl implements MyPageService{

    @Override
    public MyPageDto.MyPageResponse loadMyPage(User user) {
        MyPageDto.MyPageResponse response = new MyPageDto.MyPageResponse(user);
        return response;
    }
}
