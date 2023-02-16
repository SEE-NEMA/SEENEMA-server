package com.example.SEENEMA.post.theater.service;

import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterPostService {
    TheaterPostDto.addResponse createTheaterPost(Long userId, TheaterPostDto.addRequest request);
    List<TheaterPostDto.listResponse> listTheaterPost();
    TheaterPostDto.deleteResponse deleteTheaterPost(Long post_no);
    TheaterPostDto.addResponse editTheaterPost(Long userId, Long postNo, TheaterPostDto.addRequest request);
    TheaterPostDto.addResponse readTheaterPost(Long postNo);
    List<TheaterPostDto.listResponse> searchTheaterPost(String title);
}
