package com.example.SEENEMA.domain.seat;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageService;
import com.example.SEENEMA.domain.seat.arcoTheater.ArcoService;
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
public class SeatController {
    private final ArcoRepository arcoRepository;
    private final ArcoService arcoService;
    private final ImageService imageService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;


    @ApiOperation(value="공연장별 게시물 조회")
    @GetMapping("/{theaterId}/lists")
    public ResponseEntity getListByTheater(@PathVariable("theaterId") Long theaterId) {

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            return ResponseEntity.ok(arcoService.getListByTheater(theaterId));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
            //return ResponseEntity.ok(블루스퀘어Service.getListBySeat(theaterId, seatId));
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value="좌석별 게시물 조회")
    @GetMapping("/{theaterId}/{z}/{x}/{y}")
    public ResponseEntity getListBySeat(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y ) {

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(arcoService.getListBySeat(theaterId, seatId));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
            //블루스퀘어 seat = 블루스퀘어Repository.findByXAndY(x, y);
            //Long seatId = seat.getSeatId();
            //return ResponseEntity.ok(블루스퀘어Service.getListBySeat(theaterId, seatId));
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/{z}/{x}/{y}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SeatDto.addResponse> createViewPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @ModelAttribute SeatDto.addRequest viewDto, HttpServletRequest http) {

        Optional<User> user = findUser(http); // 토큰 읽어서 유저 정보 읽기
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            viewDto.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            viewDto.setImage(imgUrls);
        }

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(arcoService.createViewPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
            //블루스퀘어 seat = 블루스퀘어Repository.findByXAndY(x, y);
            //Long seatId = seat.getSeatId();
            //return ResponseEntity.ok(블루스퀘어Service.createViewPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}")
    public ResponseEntity readViewPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            HttpServletRequest http){

        Long seatId = null;

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            seatId = seat.getSeatId();
            return ResponseEntity.ok(arcoService.readViewPost(theaterId, seatId, viewNo));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
            //블루스퀘어 seat = 블루스퀘어Repository.findByXAndY(x, y);
            //seatId = seat.getSeatId();
            //return ResponseEntity.ok(블루스퀘어Service.readViewPost(theaterId, seatId, viewNo));
        }

        // 비로그인과 로그인 상태 구분
        String token =provider.resolveToken(http);
        if(token == null)
            return ResponseEntity.ok(arcoService.readViewPost(theaterId,seatId, viewNo));
        else{
            Optional<User> user = findUser(http);
            return ResponseEntity.ok(arcoService.readViewPost(theaterId, seatId, viewNo, user.get().getUserId()));
        }

    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}
