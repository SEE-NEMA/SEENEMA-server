package com.example.SEENEMA.post.view.controller;

import com.example.SEENEMA.post.file.service.FileService;
import com.example.SEENEMA.post.view.domain.Image;
import com.example.SEENEMA.post.view.dto.ResponseMessage;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.view.service.ViewPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/view-review")
public class ViewPostController {
    private final ViewPostServiceImpl viewPostService;
    private final FileService fileService;
    private Long userId = 1L;  // 임시 ID

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/upload" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ViewPostDto.addResponse> createViewPost(@PathVariable("theaterId") Long theaterId, @RequestParam(value="files", required = false) List<MultipartFile> files, @ModelAttribute ViewPostDto.addRequest viewDto) {
        List<Image> imgUrls = null;
        if(files != null && !files.isEmpty()) {
            imgUrls = fileService.uploadFiles(files);
            viewDto.setImage(imgUrls);
        }
        return ResponseEntity.ok(viewPostService.createViewPost(userId, theaterId, viewDto));
    }

    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{viewNo}")
    public ResponseEntity readViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo){
        return ResponseEntity.ok(viewPostService.readViewPost(userId,theaterId,viewNo));
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 수정")
    @PutMapping("/{theaterId}/{viewNo}")
    public ResponseEntity<ViewPostDto.addResponse> updateViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo,
                                                                  @RequestParam(value = "files", required = false) List<MultipartFile> files, @ModelAttribute ViewPostDto.updateRequest viewDto) {
        List<Image> imgUrls = null;
        if (files != null && !files.isEmpty()) {
            imgUrls = fileService.uploadFiles(files);
            viewDto.setImage(imgUrls);
        }
        return ResponseEntity.ok(viewPostService.updateViewPost(theaterId, viewNo, viewDto));
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 삭제")
    @DeleteMapping("/{theaterId}/{viewNo}")
    public ResponseEntity deleteViewPost(@PathVariable("theaterId") Long theaterId, @PathVariable("viewNo") Long viewNo) {
        viewPostService.deleteViewPost(theaterId, viewNo);
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
