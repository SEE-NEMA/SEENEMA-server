package com.example.SEENEMA.post.theater.controller;

import com.example.SEENEMA.comment.dto.CommentDto;
import com.example.SEENEMA.post.file.service.FileService;
import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.post.theater.service.TheaterPostServiceImpl;
import com.example.SEENEMA.post.view.domain.Image;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater-review")
public class TheaterPostController {
    @Autowired
    private TheaterPostServiceImpl service;
    private final FileService fileService;
    private Long userId = 2L;  // 임시 userId

    @ApiOperation(value = "공연장 후기 등록")
    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TheaterPostDto.addResponse> createTheaterPost(@RequestParam(value="files", required = false) List<MultipartFile> files, @ModelAttribute TheaterPostDto.addRequest request){
        List<Image> imgUrls = null;
        if(files != null && !files.isEmpty()) {
            imgUrls = fileService.uploadFiles(files);
            request.setImage(imgUrls);
        }
        return ResponseEntity.ok(service.createTheaterPost(userId, request));
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
    public ResponseEntity<TheaterPostDto.addResponse> editTheaterPost(@PathVariable Long postNo, @RequestParam(value="files", required = false) List<MultipartFile> files, @ModelAttribute TheaterPostDto.addRequest request){
        List<Image> imgUrls = null;
        if(files != null && !files.isEmpty()) {
            imgUrls = fileService.uploadFiles(files);
            request.setImage(imgUrls);
        }
        return ResponseEntity.ok(service.editTheaterPost(userId, postNo, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 조회")
    @GetMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.addResponse> readTheaterPost(@PathVariable Long postNo){
        return ResponseEntity.ok(service.readTheaterPost(postNo));
    }

    @ApiOperation(value = "공연장 후기 게시글 검색")
    @GetMapping("/search")
    public ResponseEntity<List<TheaterPostDto.listResponse>> searchTheaterPost(@RequestParam(name="q") String title){
        return ResponseEntity.ok(service.searchTheaterPost(title));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 작성")
    @PostMapping("/{postNo}/comment")
    public ResponseEntity<TheaterPostDto.addResponse> writeCommentTheaterPost(@PathVariable Long postNo, @RequestBody CommentDto.addRequest request){
        return ResponseEntity.ok(service.writeCommentTheaterPost(userId, postNo, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 수정")
    @PutMapping("/{postNo}/{commentId}")
    public ResponseEntity<TheaterPostDto.addResponse> editCommentTheaterPost(@PathVariable Long postNo, @PathVariable Long commentId, @RequestBody CommentDto.addRequest request){
        return ResponseEntity.ok(service.editCommentTheaterPost(userId, postNo, commentId, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 삭제")
    @DeleteMapping("/{postNo}/{commentId}")
    public ResponseEntity<TheaterPostDto.addResponse> deleteCommentTheaterPost(@PathVariable Long postNo, @PathVariable Long commentId){
        return ResponseEntity.ok(service.deleteCommentTheaterPost(postNo, commentId));
    }
}