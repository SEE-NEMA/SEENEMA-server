package com.example.SEENEMA.post.view.controller;


import com.example.SEENEMA.post.view.dto.ResponseMessage;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.view.repository.ViewPostRepository;
import com.example.SEENEMA.post.view.service.ViewPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;



@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/view-review")
public class ViewPostController {
    private final ViewPostServiceImpl viewPostService;
    private Long userId = 1L;  // 임시 ID
    private final ViewPostRepository viewPostRepository;

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping("/{theaterId}/upload")
    public ResponseEntity <ViewPostDto.addResponse> createViewPost (@PathVariable("theaterId") Long theaterId, @RequestBody ViewPostDto.addRequest viewDto){
        return ResponseEntity.ok(viewPostService.createViewPost(userId, theaterId,viewDto));
    }


    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{seat}/search")
    public ResponseEntity readViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("seat") String seat, @RequestParam(name="p") Long viewNo){
        return ResponseEntity.ok(viewPostService.readViewPost(userId,viewNo));
    }

    @ApiOperation(value = " 시야 리뷰 상세화면에서 수정")
    @PutMapping("/{theaterId}/{seat}/search")
    public ResponseEntity<ViewPostDto.addResponse> updateViewPost (@PathVariable("theaterId") Long theaterId, @PathVariable("seat") String seat, @RequestParam(name="p") Long viewNo, @RequestBody ViewPostDto.updateRequest viewDto){
        return ResponseEntity.ok(viewPostService.updateViewPost(theaterId,viewNo,viewDto));
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 삭제")
    @DeleteMapping("/{theaterId}/{seat}/search")
    public ResponseEntity deleteViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("seat") String seat, @RequestParam(name="p") Long viewNo) {
        viewPostService.deleteViewPost(viewNo);
        return ResponseEntity.ok(ResponseMessage.DELETE.getMsg());
    }




}
