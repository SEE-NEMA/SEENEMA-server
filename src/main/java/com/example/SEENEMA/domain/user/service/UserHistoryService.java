package com.example.SEENEMA.domain.user.service;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.mainPage.repository.ConcertRepository;
import com.example.SEENEMA.domain.mainPage.repository.MusicalRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.domain.UserHistory;
import com.example.SEENEMA.domain.user.repository.UserHistoryRepository;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final MusicalRepository musicalRepository;

    public void saveUserHistory(Long userId, Long concertId, Long musicalId) {
        User user = userRepository.findByUserId(userId);
        Concert concert = concertRepository.findByNo(concertId);
        Musical musical = musicalRepository.findByNo(musicalId);

        if (user != null && concert != null) {
            UserHistory userHistory = userHistoryRepository.findByUserUserIdAndConcertNo(userId, concertId);
            if (userHistory != null) {
                userHistory.setViewCount(userHistory.getViewCount() + 1);
            } else {
                userHistory = new UserHistory();
                userHistory.setUser(user);
                userHistory.setConcert(concert);
                userHistory.setTimestamp(LocalDateTime.now());
                userHistory.setViewCount(1); // 새로운 조회 기록이므로 초기값은 1로 설정
            }

            userHistoryRepository.save(userHistory);
        }

        if (user != null && musical != null) {
            UserHistory userHistory = userHistoryRepository.findByUserUserIdAndMusicalNo(userId, musicalId);
            if (userHistory != null) {
                userHistory.setViewCount(userHistory.getViewCount() + 1);
            } else {
                userHistory = new UserHistory();
                userHistory.setUser(user);
                userHistory.setMusical(musical);
                userHistory.setTimestamp(LocalDateTime.now());
                userHistory.setViewCount(1); // 새로운 조회 기록이므로 초기값은 1로 설정
            }

            userHistoryRepository.save(userHistory);
        }
    }


    // 사용자가 조회한 공연 중 조회 횟수가 가장 높은 공연의 장르와 캐스트를 활용하여 유사한 공연을 추천
    public List<Concert> recommendSimilarConcertsByViewCount(Long userId) {
        List<Long> mostViewedConcertIds = getMostViewedConcertIds(userId);

        if (!mostViewedConcertIds.isEmpty()) {
            Long mostViewedConcertId = mostViewedConcertIds.get(0);
            Concert mostViewedConcert = concertRepository.findById(mostViewedConcertId).orElse(null);

            if (mostViewedConcert != null) {
                String genre = mostViewedConcert.getGenre();
                String cast = mostViewedConcert.getCast();

                // 추천을 위해 해당 장르와 캐스트를 활용하여 유사한 공연을 조회
                List<Concert> recommendedConcerts = findSimilarConcertsByGenreAndCast(genre, cast);

                // 최대 4개의 추천 공연만 반환
                return recommendedConcerts.size() > 4 ? recommendedConcerts.subList(0, 4) : recommendedConcerts;
            }
        }

        return new ArrayList<>(); // 조회 횟수가 가장 높은 공연이 없는 경우 빈 리스트 반환
    }


    // 사용자가 조회한 공연 중 조회 횟수가 가장 높은 공연 ID를 가져오는 함수
    public List<Long> getMostViewedConcertIds(Long userId) {
        return userHistoryRepository.findMostViewedConcertIdsByUserId(userId);
    }

    // 콘서트의 장르와 캐스트 정보를 기반으로 유사한 콘서트를 찾는 함수
    public List<Concert> findSimilarConcertsByGenreAndCast(String genre, String cast) {
        // genre와 cast 모두를 이용하여 유사한 콘서트를 찾음
        return concertRepository.findByGenreOrCast(genre, cast);

    }
}
