package com.example.SEENEMA.user.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserRepository repo;
    private final JwtTokenProvider provider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation("회원가입 테스트")
    @PostMapping("/signup")
    public String join(@RequestBody Map<String, String> user){
        // 아이디(email), 비밀번호 양식 확인 필요
        String id = user.get("email");
//        if(repo.findByEmail(id) != null){   // 이미 존재하는 id
//            return "아이디 중복";
//        }
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

    @ApiOperation("로그인")
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user){
//        User member = repo.findByEmail(user.get("email"));
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디 입니다."));
        User member = repo.findByNickname(user.get("email"));
        if(member == null) return "가입되지 않은 아이디 입니다.";
        if(!passwordEncoder.matches(user.get("password"), member.getPassword())){
            return "잘못된 비밀번호 입니다.";
        }
        return provider.createToken(member.getUsername(), member.getRoles());
    }

    @PostMapping("/test/resource")
    public String testAPI(){
        return "hi";
    }

}
