package com.example.SEENEMA.domain.seat.arcoTheater;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageService;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoRepository;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/seats")
public class ArcoController {
    private final ArcoRepository arcoRepository;
    private final ArcoService viewPostService;
    private final ImageService imageService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;

    @GetMapping("/{id}")
    public ArcoSeat getSeat(@PathVariable("id") Long id) {

        return arcoRepository.findById(id).orElse(null);
    }

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/upload/{seatId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArcoDto.addResponse> createViewPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("seatId") Long seatId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @ModelAttribute ArcoDto.addRequest viewDto, HttpServletRequest http) {
        Optional<User> user = findUser(http); // 토큰 읽어서 유저 정보 읽기
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            viewDto.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            viewDto.setImage(imgUrls);
        }
        return ResponseEntity.ok(viewPostService.createViewPost(user.get().getUserId(), theaterId, seatId, viewDto));
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
