package com.example.SEENEMA.post.view.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.post.file.ImageService;
import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.view.dto.ResponseMessage;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.view.service.ViewPostServiceImpl;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/view-review")
public class ViewPostController {
    private final ViewPostServiceImpl viewPostService;
    private final ImageService imageService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;
    private Long userId = 1L;  // 임시 ID

    @ApiOperation(value = "시야 후기 등록 전 사용자 인증")
    @GetMapping("/auth")
    public String authUserForPost(HttpServletRequest http){
        String token = provider.resolveToken(http);
        if(token == null) return "FAIL";    // 토큰 자체가 없는 경우
         if(!provider.validateToken(token)) return "FAIL";  // 유효하지 않은 토큰인 경우
        return "SUCCESS";
    }

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/upload" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ViewPostDto.addResponse> createViewPost(@PathVariable("theaterId") Long theaterId, @RequestParam(value = "images", required = false) List<MultipartFile> images, @ModelAttribute ViewPostDto.addRequest viewDto, HttpServletRequest http) {
        Optional<User> user = findUser(http); // 토큰 읽어서 유저 정보 읽기
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            viewDto.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            viewDto.setImage(imgUrls);
        }
        return ResponseEntity.ok(viewPostService.createViewPost(user.get().getUserId(), theaterId, viewDto));
    }

    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{viewNo}")
    public ResponseEntity readViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo, HttpServletRequest http){
        // 비로그인과 로그인 상태 구분
        String token =provider.resolveToken(http);
        if(token == null) return ResponseEntity.ok(viewPostService.readViewPost(theaterId, viewNo));
//        return ResponseEntity.ok(viewPostService.readViewPost(theaterId,viewNo));
        else{
            Optional<User> user = findUser(http);
            return ResponseEntity.ok(viewPostService.readViewPost(theaterId, viewNo, user.get().getUserId()));
        }
    }
    @ApiOperation(value = "시야 리뷰 상세화면에서 좋아요 하기")
    @PostMapping("/{theaterId}/{viewNo}/heart")
    public ResponseEntity heartViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo, HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(viewPostService.heartViewPost(theaterId, viewNo, user.get().getUserId()));
    }
    @ApiOperation(value = "시야 리뷰 좋아요 취소")
    @DeleteMapping("/{theaterId}/{viewNo}/heart")
    public ResponseEntity cancelHeart(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo, HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(viewPostService.cancelHeart(theaterId, viewNo, userId));
    }

    @ApiOperation(value = "시야 리뷰 수정/삭제 전 인증")
    @GetMapping("/{theaterId}/{viewNo}/auth")
    public String authUserForEdit(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo, HttpServletRequest http){
        String basicAuth = authUserForPost(http); // 기본 인증 : 토큰 유무 / 토큰 유효성
        if(basicAuth.equals("FAIL")) return "FAIL";
        Optional<User> user = findUser(http);
        return viewPostService.authUserForEdit(theaterId, viewNo, user.get().getUserId());
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 수정")
    @PutMapping("/{theaterId}/{viewNo}")
    public ResponseEntity<ViewPostDto.addResponse> updateViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo,
                                                                  @RequestParam(value = "images", required = false) List<MultipartFile> images, @ModelAttribute ViewPostDto.updateRequest viewDto, HttpServletRequest http) {
        Optional<User> user = findUser(http);
        List<Image> imgUrls = null;


        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            viewDto.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            viewDto.setImage(imgUrls);
        }
        return ResponseEntity.ok(viewPostService.updateViewPost(theaterId, viewNo, viewDto, user.get().getUserId()));
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 삭제")
    @DeleteMapping("/{theaterId}/{viewNo}")
    public ResponseEntity deleteViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo, HttpServletRequest http) {
        Optional<User> user = findUser(http);
        String msg = viewPostService.deleteViewPost(theaterId, viewNo, user.get().getUserId());
        if(msg.equals("FAIL")) return ResponseEntity.ok("삭제 실패");
        else{
            return ResponseEntity.ok(ResponseMessage.DELETE.getMsg());
        }

    }

    @ApiOperation(value="좌석 조회")
    @GetMapping("/{theaterId}/search")
    public ResponseEntity getListBySeat(@PathVariable("theaterId") Long theaterId, @RequestParam(name="q") String seat){
        return ResponseEntity.ok(viewPostService. getListBySeat(theaterId,seat));
    }

    @ApiOperation(value="공연장별 후기 조회")
    @GetMapping("/{theaterId}")
    public ResponseEntity getListByTheater(@PathVariable("theaterId") Long theaterId){
        return ResponseEntity.ok(viewPostService. getListByTheater(theaterId));
    }
    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }


}
