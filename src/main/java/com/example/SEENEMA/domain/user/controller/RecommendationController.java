package com.example.SEENEMA.domain.user.controller;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.domain.user.service.UserHistoryService;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class RecommendationController {

    private final UserHistoryService service;
    private final JwtTokenProvider provider;
    private final UserRepository userRepo;

    @GetMapping("/user/history")
    public ResponseEntity<List<Concert>> recommendSimilarConcerts(HttpServletRequest request) {
        Optional<User> user = findUser(request);
        if (user.isPresent()) {
            List<Concert> recommendedConcerts = service.recommendSimilarConcertsByViewCount(user.get().getUserId());
            return ResponseEntity.ok(recommendedConcerts);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}

