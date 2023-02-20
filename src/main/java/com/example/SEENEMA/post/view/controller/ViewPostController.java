package com.example.SEENEMA.post.view.controller;

import com.example.SEENEMA.post.view.dto.ResponseMessage;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
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

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping("/{theaterId}/upload")
    public ResponseEntity <ViewPostDto.addResponse> createViewPost (@PathVariable("theaterId") Long theaterId, @RequestBody ViewPostDto.addRequest viewDto){
        return ResponseEntity.ok(viewPostService.createViewPost(userId, theaterId,viewDto));
    }

    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{title}/search")
    public ResponseEntity readViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("title") String title, @RequestParam(name="q") Long viewNo){
        return ResponseEntity.ok(viewPostService.readViewPost(userId,theaterId,title,viewNo));
    }

    @ApiOperation(value = " 시야 리뷰 상세화면에서 수정")
    @PutMapping("/{theaterId}/{title}/search")
    public ResponseEntity<ViewPostDto.addResponse> updateViewPost (@PathVariable("theaterId") Long theaterId, @PathVariable("title") String title, @RequestParam(name="q") Long viewNo, @RequestBody ViewPostDto.updateRequest viewDto){
        return ResponseEntity.ok(viewPostService.updateViewPost(theaterId,title,viewNo,viewDto));
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 삭제")
    @DeleteMapping("/{theaterId}/{title}/search")
    public ResponseEntity deleteViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("title") String title, @RequestParam(name="q") Long viewNo) {
        viewPostService.deleteViewPost(viewNo);
        return ResponseEntity.ok(ResponseMessage.DELETE.getMsg());
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
}
