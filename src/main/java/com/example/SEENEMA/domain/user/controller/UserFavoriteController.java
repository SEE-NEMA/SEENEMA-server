package com.example.SEENEMA.domain.user.controller;

import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.dto.UserFavoriteDto;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.domain.user.service.UserFavoriteService;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recommend")
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;

    @PostMapping
    public ResponseEntity<UserFavoriteDto.favoriteResponse> addUserFavorite(@RequestBody UserFavoriteDto.favoriteRequest request, HttpServletRequest http) {
        Optional<User> user = findUser(http); // 토큰 읽어서 유저 정보 읽기

        UserFavoriteDto.favoriteResponse response = user.map(u -> userFavoriteService.recommend(u.getUserId(), u.getNickname(), request))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return ResponseEntity.ok(response);
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }

}
