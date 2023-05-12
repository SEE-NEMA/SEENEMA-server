package com.example.SEENEMA.oauth.controller;

import com.example.SEENEMA.user.domain.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/test")
public class OAuthController {
    @ResponseBody
    @GetMapping("/oauth/loginInfo")
    public String login(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.toString();
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "TEST loginForm";
    }

//    @GetMapping("/oauth/loginInfo")
//    public String loginInfo(){
//        return "loginInfo";
//    }
//    @ResponseBody
//    @GetMapping("/oauth/login")
//    public String OAuthLogin
}
