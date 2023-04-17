package com.example.SEENEMA.user.service;

import com.example.SEENEMA.user.domain.PrincipalDetails;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.dto.GoogleUserInfo;
import com.example.SEENEMA.user.dto.KakaoUserInfo;
import com.example.SEENEMA.user.dto.OAuth2UserInfo;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository repo;

    // social site에게 받은 userRequest 데이터 후처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //"registrationId" 로 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
//        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());
        //OAuth 로그인 버튼 클릭 -> OAuth 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Clien라이브러리가 받아줌) -> code를 통해서 Token요청(access토큰 받음)
        // userRequest는 access 토큰 가지고있음
        //"loadUser" 함수-> OAuth 로부터 회원 프로필을 받음

        // OAuth 로그인, 회원가입
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"), String.valueOf(oAuth2User.getAttributes().get("id")));
        }
        else System.out.println("지원하지 않는 로그인 서비스 입니다.");

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String nickname = provider + "_" + providerId;
        String password = Base64.getEncoder().encodeToString("1234".getBytes());
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User entity;
        // 처음이용(가입)
//        if (entity == null) {
        entity = User.builder()
              .nickname(nickname)
              .password(password)
              .email(email)
              .roles(Collections.singletonList(role))
              .provider(provider)
              .providerId(providerId)
              .build();
        repo.save(entity);
//        }

        return new PrincipalDetails(entity, oAuth2User.getAttributes());
    }
}
