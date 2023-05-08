package com.example.SEENEMA.post.theater.service;

import com.example.SEENEMA.comment.dto.CommentDto;
import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterPostService {
    TheaterPostDto.addResponse createTheaterPost(Long userId, TheaterPostDto.addRequest request);
    List<TheaterPostDto.listResponse> listTheaterPost();
    TheaterPostDto.deleteResponse deleteTheaterPost(Long postNo, Long userId);
    TheaterPostDto.addResponse editTheaterPost(Long userId, Long postNo, TheaterPostDto.addRequest request);
    TheaterPostDto.addResponse readTheaterPost(Long postNo);
    TheaterPostDto.addResponse readTheaterPost(Long postNo, Long userId);
    List<TheaterPostDto.listResponse> searchTheaterPost(String title);
    TheaterPostDto.addResponse writeCommentTheaterPost(Long userId, Long postNo, CommentDto.addRequest request);
    TheaterPostDto.addResponse editCommentTheaterPost(Long userId, Long postNo, Long commentId, CommentDto.addRequest request);
    TheaterPostDto.addResponse deleteCommentTheaterPost(Long userId, Long postNo, Long commentId);
    String authUserForEdit(Long postNo, Long userId);
    String authForEditComment(Long postNo, Long commentId, Long userId);
    TheaterPostDto.addResponse heartTheaterPost(Long userId, Long postNo);
}
