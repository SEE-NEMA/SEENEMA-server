package com.example.SEENEMA.domain.user.controller;

import com.example.SEENEMA.domain.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import com.example.SEENEMA.domain.post.view.dto.ViewPostDto;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.dto.MyPageDto;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.domain.user.service.MyPageService;
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
        Optional<User> member = repo.findByEmail(user.get("email"));
        if(member.isEmpty()) return "가입되지 않은 아이디 입니다.";
        if(!passwordEncoder.matches(user.get("password"), member.get().getPassword())){
            return "잘못된 비밀번호 입니다.";
        }
        return provider.createToken(member.get().getUsername(), member.get().getRoles());
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
        return ResponseEntity.ok(service.loadMyPage(user.get()));
    }
    @ApiOperation(value = "프로필 수정을 위한 수정 전 정보 보여주는 페이지")
    @GetMapping("/profile")
    public ResponseEntity<MyPageDto.MyPageResponse> loadMyProfile(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.loadMyPage(user.get()));
    }

    @ApiOperation(value = "프로필 수정 시 닉네임 중복 체크")
    @PostMapping("/profile/check-nickname")
    public String checkNickname(HttpServletRequest http, @RequestBody MyPageDto.EditProfileRequest request){
        Optional<User> user = findUser(http);
        return service.checkNickname(user.get(), request);
    }

    @ApiOperation(value = "프로필 수정")
    @PutMapping("/profile")
    public ResponseEntity<MyPageDto.MyPageResponse> editProfile(HttpServletRequest http, @RequestBody MyPageDto.EditProfileRequest request){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.editProfile(user.get(), request));
    }
    @ApiOperation(value = "내가 쓴 공연장 후기 목록")
    @GetMapping("/my-review/theater")
    public ResponseEntity<List<TheaterPostDto.listResponse>> listMyTheaterReview(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.listMyTheaterReview(user.get()));
    }
    @ApiOperation(value = "내가 쓴 공연장 후기 댓글 목록")
    @GetMapping("/my-review/theater/comment")
    public ResponseEntity<List<MyPageDto.MyCommentList>> listMyComment(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.listMyComment(user.get()));
    }
    @ApiOperation(value = "내가 작성한 시야후기 목록")
    @GetMapping("/my-review/view")
    public ResponseEntity<List<ViewPostDto.viewListResponse>> listMyViewReview(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.listMyViewReview(user.get()));
    }
    @ApiOperation(value = "내가 좋아요한 공연장 후기")
    @GetMapping("/my-heart/theater")
    public ResponseEntity<List<TheaterPostDto.listResponse>> listHeartedTheaterReview(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.listHeartedTheaterReview(user.get()));
    }
    @ApiOperation(value = "내가 좋아요한 시야 후기")
    @GetMapping("/my-heart/view")
    public ResponseEntity<List<ViewPostDto.viewListResponse>> listHeartedViewReview(HttpServletRequest http){
        Optional<User> user = findUser(http);
        return ResponseEntity.ok(service.listHeartedViewReview(user.get()));
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return repo.findByEmail(provider.getUserPk(token));
    }
}
