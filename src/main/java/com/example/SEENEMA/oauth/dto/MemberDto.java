package com.example.SEENEMA.oauth.dto;

import com.example.SEENEMA.oauth.domain.Member;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {
    @Getter
    @Setter
    public class MemberProfile{
        private String name;
        private String email;
        private String provider;
        private String nickname;
        public Member toEntity(){
            return Member.builder()
                    .name(name)
                    .email(email)
                    .provider(provider)
                    .nickname(email) // 초기 닉네임은 이메일로 설정
                    .build();
        }
    }
}
