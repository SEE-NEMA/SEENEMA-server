package com.example.SEENEMA.domain.user.controller;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import com.example.SEENEMA.domain.user.service.UserHistoryService;
import com.example.SEENEMA.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public ResponseEntity<?> recommendSimilarConcerts(HttpServletRequest request) {
        Optional<User> user = findUser(request);
        if (user.isPresent()) {
            try {
                List<Concert> recommendedConcerts = service.recommendSimilarConcertsByViewCount(user.get().getUserId());
                return ResponseEntity.ok(recommendedConcerts);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while recommending concerts");
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

//    @GetMapping("/user/history")
//    public ResponseEntity<?> recommendSimilarPerformances(HttpServletRequest request) {
//        Optional<User> user = findUser(request);
//        if (user.isPresent()) {
//            try {
//                List<Concert> recommendedConcerts = service.recommendSimilarConcertsByViewCount(user.get().getUserId());
//                System.out.println("### r1:"+recommendedConcerts);
//                List<Musical> recommendedMusicals = service.recommendSimilarMusicalsByViewCount(user.get().getUserId());
//                System.out.println("### r2:"+recommendedMusicals);
//
//                // Combine the recommendations
//                List<Object> combinedRecommendations = new ArrayList<>();
//                combinedRecommendations.addAll(recommendedConcerts);
//                combinedRecommendations.addAll(recommendedMusicals);
//                System.out.println("### combined: "+combinedRecommendations);
//
//                return ResponseEntity.ok(combinedRecommendations);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while recommending performances");
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//    }




    private Optional<User> findUser(HttpServletRequest request){
        String token = provider.resolveToken(request);
        return userRepo.findByEmail(provider.getUserPk(token));
    }
}
