package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.view.dto.ViewPostDto;
import org.springframework.stereotype.Service;

@Service
public interface ViewPostService {

    ViewPostDto.addResponse createView(Long userId, Long theaterId, ViewPostDto.addRequest requestDto);


}