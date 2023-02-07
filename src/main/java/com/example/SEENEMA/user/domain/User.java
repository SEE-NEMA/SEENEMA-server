package com.example.SEENEMA.user.domain;

import lombok.*;
import javax.persistence.*;


@Entity
@Getter
@Table(name = "USER")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Builder
    public User(int user_id, String email, String password, String nickname) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}