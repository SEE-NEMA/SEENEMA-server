package com.example.SEENEMA.user.dto;

import lombok.*;


@Setter
@Getter
public class UserDto {
    private Long user_id;
    private String email;
    private String password;
    private String nickname;

}
