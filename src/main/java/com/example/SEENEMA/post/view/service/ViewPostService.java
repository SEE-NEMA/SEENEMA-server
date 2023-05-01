package com.example.SEENEMA.post.view.service;


import com.example.SEENEMA.post.view.dto.ViewPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ViewPostService {

    ViewPostDto.addResponse createViewPost(Long userId, Long theaterId, ViewPostDto.addRequest requestDto);
    ViewPostDto.detailResponse readViewPost(Long userId, Long theaterId, Long viewNo);
    ViewPostDto.addResponse updateViewPost(Long theaterId, Long viewNo, ViewPostDto.updateRequest requestDto);
    void deleteViewPost(Long theaterId, Long viewNo);

    List<ViewPostDto.viewListResponse>  getListBySeat(Long theaterId, String seatName);
    List<ViewPostDto.viewListResponse>  getListByTheater(Long theaterId);
    String authUserForEdit(Long theaterId, Long viewNo, Long userId);

}