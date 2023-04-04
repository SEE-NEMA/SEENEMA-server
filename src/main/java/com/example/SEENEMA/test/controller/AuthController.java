package com.example.SEENEMA.test.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.dto.UserDto;
import com.example.SEENEMA.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test1")
public class AuthController {
    private final UserRepository repo;
    private final JwtTokenProvider provider;
    private final PasswordEncoder passwordEncoder;

//    public ResponseEntity<String> signup(@RequestBody User user){
//        String token = provider.createToken();
//        return ResponseEntity.ok(token);
//    }


//    @ApiOperation("회원가입")
//    @PostMapping("/join")
//    public Long join(@RequestBody UserDto.RequestJoinUser request){
//        return repo.save(User.builder()
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .nickname(request.getNickname())
//                .roles(Collections.singletonList("ROLE_USER"))
//                .build()).getUserId();
//    }


    @ApiOperation("회원가입 테스트")
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user){
        return repo.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .nickname("test")
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getUserId();
    }
}
