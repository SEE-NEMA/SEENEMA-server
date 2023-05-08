package com.example.SEENEMA.post.view.service;


import com.example.SEENEMA.post.view.dto.ViewPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ViewPostService {

    ViewPostDto.addResponse createViewPost(Long userId, Long theaterId, ViewPostDto.addRequest requestDto);
    ViewPostDto.detailResponse readViewPost(Long theaterId, Long viewNo);
    ViewPostDto.detailResponse readViewPost(Long theaterId, Long viewNo, Long userId);
    ViewPostDto.addResponse updateViewPost(Long theaterId, Long viewNo, ViewPostDto.updateRequest requestDto, Long userId);
    String deleteViewPost(Long theaterId, Long viewNo, Long userId);

    List<ViewPostDto.viewListResponse>  getListBySeat(Long theaterId, String seatName);
    List<ViewPostDto.viewListResponse>  getListByTheater(Long theaterId);
    String authUserForEdit(Long theaterId, Long viewNo, Long userId);
    ViewPostDto.detailResponse heartViewPost(Long theaterId, Long viewNo, Long userId);

}