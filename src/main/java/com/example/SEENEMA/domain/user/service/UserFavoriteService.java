package com.example.SEENEMA.domain.user.service;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.mainPage.repository.ConcertRepository;
import com.example.SEENEMA.domain.mainPage.repository.MusicalRepository;
import com.example.SEENEMA.domain.user.dto.UserFavoriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserFavoriteService {

    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;

    public UserFavoriteDto.favoriteResponse recommend(Long userId, String nickName, UserFavoriteDto.favoriteRequest request) {
        List<Concert> recommendedConcerts = new ArrayList<>();
        List<Musical> recommendedMusicals = new ArrayList<>();

        if (request.getConcertFavorites() != null && !request.getConcertFavorites().isEmpty()) {
            for (Concert concert : request.getConcertFavorites()) {
                List<Concert> concertsByCast = concertRepository.findByCastContaining(concert.getCast());
                List<Concert> concertsByGenre = concertRepository.findByGenreContaining(concert.getGenre());

                if (!concertsByCast.isEmpty()) {
                    recommendedConcerts.add(concertsByCast.get(0));
                } else {
                    recommendedConcerts.add(null); // cast에 해당하는 공연 없음 표시
                }

                if (!concertsByGenre.isEmpty()) {
                    recommendedConcerts.add(concertsByGenre.get(0));
                } else {
                    recommendedConcerts.add(null); // genre에 해당하는 공연 없음 표시
                }
            }
        }

        if (request.getMusicalFavorites() != null && !request.getMusicalFavorites().isEmpty()) {
            for (Musical musical : request.getMusicalFavorites()) {
                List<Musical> musicalsByCast = musicalRepository.findByCastContaining(musical.getCast());
                List<Musical> musicalsByGenre = musicalRepository.findByGenreContaining(musical.getGenre());

                if (!musicalsByCast.isEmpty()) {
                    recommendedMusicals.add(musicalsByCast.get(0));
                } else {
                    recommendedMusicals.add(null); // cast에 해당하는 공연 없음 표시
                }

                if (!musicalsByGenre.isEmpty()) {
                    recommendedMusicals.add(musicalsByGenre.get(0));
                } else {
                    recommendedMusicals.add(null); // genre에 해당하는 공연 없음 표시
                }
            }
        }

        UserFavoriteDto.favoriteResponse response = new UserFavoriteDto.favoriteResponse(userId, nickName, recommendedConcerts, recommendedMusicals);

        return response;
    }


}