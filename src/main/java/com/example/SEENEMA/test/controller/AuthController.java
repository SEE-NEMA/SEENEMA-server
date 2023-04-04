package com.example.SEENEMA.test.controller;

import com.example.SEENEMA.jwt.JwtTokenProvider;
import com.example.SEENEMA.test.service.UserService;
import com.example.SEENEMA.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider provider;

//    public ResponseEntity<String> signup(@RequestBody User user){
//        String token = provider.generateToken(userService.createUser(user));
//        return ResponseEntity.ok(token);
//    }
}
