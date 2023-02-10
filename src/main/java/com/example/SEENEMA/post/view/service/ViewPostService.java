package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.view.domain.ViewPost;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import org.springframework.stereotype.Service;

import javax.swing.text.View;

@Service
public interface ViewPostService {

    ViewPostDto.addResponse createViewPost(Long userId, Long theaterId, ViewPostDto.addRequest requestDto);

    ViewPostDto.addResponse updateViewPost(Long theaterId, Long viewNo, ViewPostDto.updateRequest requestDto);

    void deleteViewPost(Long viewNo);

}