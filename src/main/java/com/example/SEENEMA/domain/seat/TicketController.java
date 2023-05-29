package com.example.SEENEMA.domain.seat;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageService;
import com.example.SEENEMA.domain.seat.arcoTheater.ArcoService;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoRepository;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.ShinhanService;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanSeat;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanRepository;
import com.example.SEENEMA.domain.seat.chungmu.ChungmuService;
import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuSeat;
import com.example.SEENEMA.domain.seat.chungmu.repository.ChungmuRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
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
@RequestMapping("/api/v1/view-review")
public class TicketController {

    private final ArcoRepository arcoRepository;
    private final ArcoService arcoService;
    private final ShinhanRepository shinhanRepository;
    private final ShinhanService shinhanService;
    private final ChungmuRepository chungmuRepository;
    private final ChungmuService chungmuService;

    private final ImageService imageService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;


    @ApiOperation(value = "시야 후기 등록")
    @PostMapping(value="/{theaterId}/{z}/{x}/{y}/ticket/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
            return ResponseEntity.ok(shinhanService.createSeatPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        /** 충무아트센터 대극장 */
        else if (theaterId == 30) {
            ChungmuSeat seat = chungmuRepository.findByXAndYAndZ(x, y, z);
            Long seatId = seat.getSeatId();
            return ResponseEntity.ok(chungmuService.createSeatPost(user.get().getUserId(), theaterId, seatId, viewDto));
        }
        return ResponseEntity.notFound().build();
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}
