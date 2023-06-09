package com.example.SEENEMA.domain.user.domain;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USER_FAVORITE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_concert",
            joinColumns = @JoinColumn(name = "favorite_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "concert_cast", referencedColumnName = "cast"),
                    @JoinColumn(name = "concert_genre", referencedColumnName = "genre")
            }
    )
    private List<Concert> concertFavorites;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_musical",
            joinColumns = @JoinColumn(name = "favorite_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "musical_cast", referencedColumnName = "cast"),
                    @JoinColumn(name = "musical_genre", referencedColumnName = "genre")
            }
    )
    private List<Musical> musicalFavorites;

}