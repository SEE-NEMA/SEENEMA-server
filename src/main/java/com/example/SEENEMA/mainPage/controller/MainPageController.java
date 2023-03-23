package com.example.SEENEMA.mainPage.controller;


import com.example.SEENEMA.mainPage.dto.MainPageDto;
import com.example.SEENEMA.mainPage.dto.PlayDto;
import com.example.SEENEMA.mainPage.repository.MusicalRepository;
import com.example.SEENEMA.mainPage.service.MainPageServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainPageController {
    @Autowired
    private MainPageServiceImpl service;

    @ApiOperation(value = "SEE-NEMA 메인페이지")
    @GetMapping("/")
    public ResponseEntity<MainPageDto.responseDTO> readRanking(){
        return ResponseEntity.ok(service.readRanking());
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

}
