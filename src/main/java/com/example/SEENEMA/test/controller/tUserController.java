package com.example.SEENEMA.test.controller;

import com.example.SEENEMA.logIn.JwtTokenProvider;
import com.example.SEENEMA.test.domain.tUser;
import com.example.SEENEMA.test.repository.tUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class tUserController {
    @Autowired
    private final JwtTokenProvider provider;
    @Autowired
    private tUserRepository repo;

    tUser tuser = tUser.builder()
            .email("jiyoon@abc.com")
            .password("1234")
            .nickname("jiyooni")
            .roles(Collections.singletonList("ROLE_USER"))
            .build();

    @PostMapping("/signin")
    public String join(){
        log.info("로그인 시도");
        repo.save(tuser);
        return tuser.toString();
    }
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user){
        log.info("user email = "+user.get("email"));
        tUser member = repo.findByUserEmail(user.get("email")).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-Mail 입니다."));
        return provider.createToken(member.getUsername(), member.getRoles());
    }
}
