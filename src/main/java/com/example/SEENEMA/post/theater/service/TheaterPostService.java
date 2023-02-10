package com.example.SEENEMA.post.theater.service;

import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterPostService {
    TheaterPostDto.addResponse createTheaterPost(Long userId, TheaterPostDto.addRequest request);
    List<TheaterPostDto.listResponse> listTheaterPost();
}
