package com.example.SEENEMA.mainPage.controller;

import com.example.SEENEMA.mainPage.dto.MainPageDto;
import com.example.SEENEMA.mainPage.service.MainPageServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainPageController {
    @Autowired
    private MainPageServiceImpl service;
    private Long uerId = 2L;    // 로그인 기능없어서 임시로 만들어놓은 userId
    @ApiOperation(value = "SEE-NEMA 메인페이지")
    @GetMapping("/")
    public ResponseEntity<MainPageDto.readRanking> readRanking(){
        return ResponseEntity.ok(service.readRanking());
    }
}
