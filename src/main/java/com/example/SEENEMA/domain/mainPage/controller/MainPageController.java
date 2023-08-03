package com.example.SEENEMA.domain.mainPage.controller;


import com.example.SEENEMA.domain.mainPage.dto.MainPageDto;
import com.example.SEENEMA.domain.mainPage.dto.PlayDto;
import com.example.SEENEMA.domain.mainPage.service.MainPageServiceImpl;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.domain.user.service.UserHistoryService;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainPageController {

    private final MainPageServiceImpl service;
    private final UserHistoryService userHistoryService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;

    @ApiOperation(value = "SEE-NEMA 메인페이지")
    @GetMapping("/")
    public ResponseEntity<MainPageDto> readRanking() {
        MainPageDto response = new MainPageDto();

        List<MainPageDto.concertRanking> concertRankings = service.getConcertRankings();
        response.setConcertRank(concertRankings);

        List<MainPageDto.musicalRanking> musicalRankings = service.getMusicalRankings();
        response.setMusicalRank(musicalRankings);

        return ResponseEntity.ok(response);
    }
    @ApiOperation(value = "뮤지컬 목록")
    @GetMapping("/musicals")
    public ResponseEntity<List<PlayDto.musicalList>> getMusicalList() {
        return ResponseEntity.ok(service.getMusicalList());
    }
    @ApiOperation(value = "뮤지컬 상세정보")
    @GetMapping("/musicals/{no}")
    public ResponseEntity<PlayDto.musicalInfo> getMusicalInfo(@PathVariable("no") Long no){
        return ResponseEntity.ok(service.getMusicalInfo(no));
    }

    @ApiOperation(value = "콘서트 목록")
    @GetMapping("/concerts")
    public ResponseEntity<List<PlayDto.concertList>> getConcertList() {
        return ResponseEntity.ok(service.getConcertList());
    }

    @ApiOperation(value = "콘서트 상세정보")
    @GetMapping("/concerts/{no}")
    public ResponseEntity<PlayDto.concertInfo> getConcertInfo(@PathVariable("no") Long no, HttpServletRequest request) {
        Optional<User> user = findUser(request);
        PlayDto.concertInfo concertInfo = service.getConcertInfo(no);
        if (concertInfo != null && user.isPresent()) {
            // 콘서트 조회 시 UserHistory에 저장
            userHistoryService.saveUserHistory(user.get().getUserId(), no);
        }
        else
            System.out.println(user.get().getUserId()+"저장 실패");

        return ResponseEntity.ok(concertInfo);
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }

}
