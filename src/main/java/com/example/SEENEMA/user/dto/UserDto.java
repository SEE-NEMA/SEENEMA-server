package com.example.SEENEMA.user.dto;

import com.example.SEENEMA.user.domain.User;
import lombok.*;
import org.springframework.context.annotation.Bean;


@Setter
@Getter
public class UserDto {
    @Getter
    public class RequestJoinUser {
        private String email;
        private String password;
        private String nickname;

//        public User toEntity(){
//            return User.builder()
//                    .userId(user_id)
//                    .email(email)
//                    .password(password)
//                    .nickname(nickname)
//                    .build();
//        }
    }
}
