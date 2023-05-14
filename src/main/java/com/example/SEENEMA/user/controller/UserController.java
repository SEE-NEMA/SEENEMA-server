package com.example.SEENEMA.user.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.dto.MyPageDto;
import com.example.SEENEMA.user.repository.UserRepository;
import com.example.SEENEMA.user.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserRepository repo;
    private final JwtTokenProvider provider;
    private final PasswordEncoder passwordEncoder;
    private final MyPageService service;

    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public String join(@RequestBody Map<String, String> user){
        // 아이디(email), 비밀번호 양식 확인 필요
        String id = user.get("email");
        List<User> userList = repo.findAll();
        for(User u : userList){
            if(u.getEmail().equals(id)) return "아이디 중복";
        }
        repo.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .nickname(user.get("email"))    // 초기 nickname=email자동설정
                .roles(Collections.singletonList("ROLE_USER")) // 기본 사용자는 USER로 설정
                .build());
        return "회원가입 정상 완료";
    }
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user){
        User member = repo.findByNickname(user.get("email"));
        if(member == null) return "가입되지 않은 아이디 입니다.";
        if(!passwordEncoder.matches(user.get("password"), member.getPassword())){
            return "잘못된 비밀번호 입니다.";
        }
        return provider.createToken(member.getUsername(), member.getRoles());
    }
    @ApiOperation(value = "인가 테스트")
    @PostMapping("/test/resource")
    public String testAPI(){
        return "hi";
    }

    @ApiOperation(value = "마이페이지")
    @GetMapping("/mypage")
    public ResponseEntity<MyPageDto.MyPageResponse> loadMyPage(HttpServletRequest http){
        Optional<User> user = findUser(http);
        System.out.println();
        return ResponseEntity.ok(service.loadMyPage(user.get()));
    }
    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return repo.findByEmail(provider.getUserPk(token));
    }
}
