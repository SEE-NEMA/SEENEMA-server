package com.example.SEENEMA.post.view.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // 생성자 주입
public class TestController {

    /** nginx 테스트용 api */
    @GetMapping("/")
    public String nginx(){
        return "nginx";
    }

    /** 배포 테스트용 api */
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
