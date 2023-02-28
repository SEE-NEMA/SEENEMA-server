package com.example.SEENEMA.theater.controller;

import com.example.SEENEMA.theater.service.TheaterServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/v1/view-review")
public class TheaterController {

    private final TheaterServiceImpl theaterService;

    @ApiOperation(value="공연장 조회")
    @GetMapping("/search")
    public ResponseEntity searchTheater(@RequestParam(name="q") String theaterName){
        return ResponseEntity.ok(theaterService. searchTheater(theaterName));
    }
}
