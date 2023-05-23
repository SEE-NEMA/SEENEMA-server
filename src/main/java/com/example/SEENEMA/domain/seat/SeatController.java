package com.example.SEENEMA.domain.seat;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageService;
import com.example.SEENEMA.domain.post.view.dto.ResponseMessage;
import com.example.SEENEMA.domain.seat.arcoTheater.ArcoService;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.ShinhanService;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanSeat;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanRepository;
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
    private final ShinhanRepository shinhanRepository;
    private final ShinhanService shinhanService;
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
        }

        return ResponseEntity.notFound().build();
    }
    @ApiOperation(value = "시야 후기 등록 전 사용자 인증")
    @PostMapping("/auth")
    public String authUserForPost(HttpServletRequest http){
        String token = provider.resolveToken(http);
        if(token == null) return "FAIL";    // 토큰 자체가 없는 경우
        if(!provider.validateToken(token)) return "FAIL";  // 유효하지 않은 토큰인 경우
        return "SUCCESS";
    }

    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/{z}/{x}/{y}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SeatDto.addResponse> createSeatPost(
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
            return ResponseEntity.ok(arcoService.createSeatPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
            ShinhanSeat seat =shinhanRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(shinhanService.createViewPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value="시야 리뷰 상세화면")
    @GetMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}")
    public ResponseEntity readSeatPost(
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
            return ResponseEntity.ok(arcoService.readSeatPost(theaterId, seatId, viewNo));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        // 비로그인과 로그인 상태 구분
        String token =provider.resolveToken(http);

        if (token == null) {
            switch (theaterId.intValue()) {
                case 37: // 아르코 예술극장
                    return ResponseEntity.ok(arcoService.readSeatPost(theaterId, seatId, viewNo));
                case 12: // 블루스퀘어 신한카드홀

                default:
                    return ResponseEntity.badRequest().build();
            }
        } else {
            Optional<User> user = findUser(http);
            switch (theaterId.intValue()) {
                case 37:
                    return ResponseEntity.ok(arcoService.readSeatPost(theaterId, seatId, viewNo, user.get().getUserId()));
                case 12:

                default:
                    return ResponseEntity.badRequest().build();
            }
        }

    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 좋아요 하기")
    @PostMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}/heart")
    public ResponseEntity heartSeatPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            HttpServletRequest http){

        Optional<User> user = findUser(http);

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            ResponseEntity.ok(arcoService.heartSeatPost(theaterId, seatId, viewNo, user.get().getUserId()));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        return ResponseEntity.notFound().build();
    }
    @ApiOperation(value = "시야 리뷰 좋아요 취소")
    @DeleteMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}/heart")
    public ResponseEntity cancelHeart(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            HttpServletRequest http){

        Optional<User> user = findUser(http);

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(arcoService.cancelHeart(theaterId, seatId, viewNo, user.get().getUserId()));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "시야 리뷰 수정/삭제 전 인증")
    @PostMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}/auth")
    public String authUserForEdit(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            HttpServletRequest http){

        String basicAuth = authUserForPost(http); // 기본 인증 : 토큰 유무 / 토큰 유효성
        if(basicAuth.equals("FAIL")) return "FAIL";
        Optional<User> user = findUser(http);

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return arcoService.authUserForEdit(theaterId, seatId, viewNo, user.get().getUserId());
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        return "NOT FOUND";
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 수정")
    @PutMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}")
    public ResponseEntity<SeatDto.addResponse> updateSeatPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @ModelAttribute SeatDto.updateRequest seatDto,
            HttpServletRequest http) {

        Optional<User> user = findUser(http);
        List<Image> imgUrls = null;

        if(images != null && !images.isEmpty()) {
            imgUrls = imageService.uploadFiles(images);
            seatDto.setImage(imgUrls);
        } else {
            imgUrls = new ArrayList<>();
            seatDto.setImage(imgUrls);
        }

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            ArcoSeat seat = arcoRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(arcoService.updateSeatPost(theaterId, seatId, viewNo, seatDto, user.get().getUserId()));
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "시야 리뷰 상세화면에서 삭제")
    @DeleteMapping("/{theaterId}/{z}/{x}/{y}/{viewNo}")
    public ResponseEntity deleteSeatPost(
            @PathVariable("theaterId") Long theaterId,
            @PathVariable("z") int z,
            @PathVariable("x") int x,
            @PathVariable("y") int y,
            @PathVariable("viewNo") Long viewNo,
            HttpServletRequest http) {

        Optional<User> user = findUser(http);
        String msg = null;
        Long seatId = null;

        /** 아르코 예술극장 */
        if (theaterId == 37) {
            msg = arcoService.deleteSeatPost(theaterId, seatId, viewNo, user.get().getUserId());
        }
        /** 블루스퀘어 신한카드홀 */
        else if (theaterId == 12) {
        }

        if(msg.equals("FAIL"))
            return ResponseEntity.ok("삭제 실패");
        else{
            return ResponseEntity.ok(ResponseMessage.DELETE.getMsg());
        }
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}
