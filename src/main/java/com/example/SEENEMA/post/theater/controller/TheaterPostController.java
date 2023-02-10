package com.example.SEENEMA.post.theater.controller;

import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.post.theater.service.TheaterPostServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater-review")
public class TheaterPostController {
    @Autowired
    private TheaterPostServiceImpl service;

    @ApiOperation(value = "공연장 후기 등록")
    @PostMapping("/upload")
    public ResponseEntity<TheaterPostDto.addResponse> createTheaterPost(@RequestBody TheaterPostDto.addRequest request){
        return ResponseEntity.ok(service.createTheaterPost(2L, request));
    }

    @ApiOperation(value = "공연장 후기 게시물 메인 페이지")
    @GetMapping("/")
    public ResponseEntity<List<TheaterPostDto.listResponse>> mainTheaterPost(){
        return ResponseEntity.ok(service.listTheaterPost());
    }

    @ApiOperation(value = "공연장 후기 게시물 삭제")
    @DeleteMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.deleteResponse> deleteTheaterPost(@PathVariable Long postNo){
        return ResponseEntity.ok(service.deleteTheaterPost(postNo));
    }

    @ApiOperation(value = "공연장 후기 게시물 수정")
    @PutMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.addResponse> editTheaterPost(@PathVariable Long postNo, @RequestBody TheaterPostDto.addRequest request){
        return ResponseEntity.ok(service.editTheaterPost(2L, postNo, request));
    }
}
