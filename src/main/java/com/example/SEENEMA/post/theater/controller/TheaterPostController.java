package com.example.SEENEMA.post.theater.controller;

import com.example.SEENEMA.comment.dto.CommentDto;
import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.post.file.ImageService;
import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.post.theater.service.TheaterPostServiceImpl;
import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theater-review")
public class TheaterPostController {
    private final TheaterPostServiceImpl service;
    private final ImageService imageService;
    private final UserRepository userRepo;
    private final JwtTokenProvider provider;
    private Long userId = 2L;  // 임시 userId

    @ApiOperation(value = "게시글과 댓글 등록 전 사용자 인증")
    @PostMapping("/auth")
    public String authUserForPosting(HttpServletRequest http){
        String token = provider.resolveToken(http);
        if(token == null) return "FAIL";    // 토큰 자체가 없는 경우 -> fail
        if(!provider.validateToken(token)) return "FAIL";   // 유효하지 않은 토큰 -> fail
        return "SUCCESS";
    }

    @ApiOperation(value = "공연장 후기 등록")
    @PostMapping(value="/upload")
    public ResponseEntity<TheaterPostDto.addResponse> createTheaterPost(@RequestParam(value = "images", required = false) List<MultipartFile> images, @ModelAttribute TheaterPostDto.addRequest request, HttpServletRequest http){
        Optional<User> user = findUser(http);
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            request.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            request.setImage(imgUrls);
        }
        return ResponseEntity.ok(service.createTheaterPost(user.get().getUserId(), request));
    }

    @ApiOperation(value = "공연장 후기 게시물 메인 페이지")
    @GetMapping("/")
    public ResponseEntity<List<TheaterPostDto.listResponse>> mainTheaterPost(){
        return ResponseEntity.ok(service.listTheaterPost());
    }

    @ApiOperation(value="태그별 게시물 모아보기")
    @GetMapping("/tags/{tagId}")
    public ResponseEntity<List<TheaterPostDto.listResponse>> getTheaterPostByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(service.listTheaterPostByTag(tagId));
    }

    @ApiOperation(value = "게시글 수정/삭제 시 사용자 인증")
    @PostMapping("/{postNo}/auth")
    public String authUserForEdit(@PathVariable Long postNo, HttpServletRequest http){
        String basicAuth = authUserForPosting(http); // 기본 인증 : 토큰 유무 / 토큰 유효성
        if(basicAuth.equals("FAIL")) return "FAIL";
        Optional<User> user = findUser(http);
        return service.authUserForEdit(postNo, user.get().getUserId());
    }

    @ApiOperation(value = "공연장 후기 게시물 삭제")
    @DeleteMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.deleteResponse> deleteTheaterPost(@PathVariable Long postNo, HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.deleteTheaterPost(postNo,user.get().getUserId()));
    }

    @ApiOperation(value = "공연장 후기 게시물 수정")
    @PutMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.addResponse> editTheaterPost(@PathVariable Long postNo, @RequestParam(value = "images", required = false) List<MultipartFile> images, @ModelAttribute TheaterPostDto.addRequest request, HttpServletRequest http){
        Optional<User> user = findUser(http);
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            request.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            request.setImage(imgUrls);
        }
        return ResponseEntity.ok(service.editTheaterPost(user.get().getUserId(), postNo, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 조회")
    @GetMapping("/{postNo}")
    public ResponseEntity<TheaterPostDto.addResponse> readTheaterPost(@PathVariable Long postNo, HttpServletRequest http){
        // 로그인 한 사용자가 게시글을 조회하는 경우와 비로그인 상태 구분
        String token = provider.resolveToken(http);
        if(token==null) return ResponseEntity.ok(service.readTheaterPost(postNo));
        else {
            Optional<User> user = findUser(http);
            return ResponseEntity.ok(service.readTheaterPost(postNo, user.get().getUserId()));
        }
    }

    @ApiOperation(value = "공연장 후기 게시글 좋아요")
    @PostMapping("/{postNo}/heart")
    public ResponseEntity<TheaterPostDto.addResponse> heartTheaterPost(@PathVariable Long postNo, HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.heartTheaterPost(user.get().getUserId(), postNo));
    }
    @ApiOperation(value = "공연장 후기 좋아요 취소")
    @DeleteMapping("/{postNo}/heart")
    public ResponseEntity<TheaterPostDto.addResponse> cancelHeart(@PathVariable Long postNo, HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.cancelHeart(user.get().getUserId(), postNo));
    }

    @ApiOperation(value = "공연장 후기 게시글 검색")
    @GetMapping("/search")
    public ResponseEntity<List<TheaterPostDto.listResponse>> searchTheaterPost(@RequestParam(name="q") String title){
        return ResponseEntity.ok(service.searchTheaterPost(title));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 작성")
    @PostMapping("/{postNo}/comment")
    public ResponseEntity<TheaterPostDto.addResponse> writeCommentTheaterPost(@PathVariable Long postNo, @RequestBody CommentDto.addRequest request, HttpServletRequest http){
        Optional<User> user = findUser(http); // /auth API를 통해 인증 후 작성하므로 별다른 인증 진행 X
        return ResponseEntity.ok(service.writeCommentTheaterPost(user.get().getUserId(), postNo, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 수정/삭제 전 인증")
    @PostMapping("/{postNo}/{commentId}/auth")
    public String authForEditComment(@PathVariable Long postNo, @PathVariable Long commentId, HttpServletRequest http){
        String basicAuth = authUserForPosting(http); // 기본 인증 : 토큰 유무 / 토큰 유효성
        if(basicAuth.equals("FAIL")) return "FAIL";
        Optional<User> user = findUser(http);
        return service.authForEditComment(postNo, commentId, user.get().getUserId());
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 수정")
    @PutMapping("/{postNo}/{commentId}")
    public ResponseEntity<TheaterPostDto.addResponse> editCommentTheaterPost(@PathVariable Long postNo, @PathVariable Long commentId, @RequestBody CommentDto.addRequest request, HttpServletRequest http){
        Optional<User> user = findUser(http); // /auth API를 통해 인증 후 작성하므로 별다른 인증 진행 X
        return ResponseEntity.ok(service.editCommentTheaterPost(user.get().getUserId(), postNo, commentId, request));
    }

    @ApiOperation(value = "공연장 후기 게시글 댓글 삭제")
    @DeleteMapping("/{postNo}/{commentId}")
    public ResponseEntity<TheaterPostDto.addResponse> deleteCommentTheaterPost(@PathVariable Long postNo, @PathVariable Long commentId, HttpServletRequest http){
        Optional<User> user = findUser(http); // /auth API를 통해 인증 후 작성하므로 별다른 인증 진행 X
        return ResponseEntity.ok(service.deleteCommentTheaterPost(user.get().getUserId(), postNo, commentId));
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}