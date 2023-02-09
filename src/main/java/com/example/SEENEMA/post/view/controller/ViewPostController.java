package com.example.SEENEMA.post.view.controller;


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
    private final ViewPostServiceImpl viewService;
    private Long userId = 1L;  // 로그인 기능없어서 임시로 만들어놓은 userId

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping("/{theaterId}/upload")
    public ResponseEntity <ViewPostDto.addResponse> createView (@PathVariable("theaterId") Long theaterId, @RequestBody ViewPostDto.addRequest viewDto){
        return ResponseEntity.ok(viewService.createView(userId, theaterId,viewDto));
    }

}
