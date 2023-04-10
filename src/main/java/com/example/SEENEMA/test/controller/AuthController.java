package com.example.SEENEMA.test.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.user.domain.PrincipalDetails;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class AuthController {
    private final UserRepository repo;
    private final JwtTokenProvider provider;
    private final PasswordEncoder passwordEncoder;

//    private final BCryptPasswordEncoder encoder;
    @ApiOperation("회원가입 테스트")
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user){
        return repo.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .nickname(user.get("email"))    // 초기 nickname=email
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getUserId();
    }

    @ApiOperation("로그인")
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user){
        User member = repo.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        if(!passwordEncoder.matches(user.get("password"), member.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return provider.createToken(member.getUsername(), member.getRoles());
    }

    @ResponseBody
    @GetMapping("/login")
    public String login(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return "세션 정보 확인";
    }
//    @ResponseBody
//    @GetMapping("/oauth/login")
//    public String OAuthLogin
}
