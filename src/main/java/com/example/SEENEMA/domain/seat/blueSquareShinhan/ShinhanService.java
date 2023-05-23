package com.example.SEENEMA.domain.seat.blueSquareShinhan;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanSeat;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanPostRepository;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShinhanService {
    private final ShinhanRepository shinhanRepository;
    private final ShinhanPostRepository shinhanPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;
    public static String convertToSeatNumber(int x, int y, int z) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용
        String floor = String.valueOf(z); // 층은 z 좌표 그대로 사용

        return floor + "층 " + column + "열 " + number + "번";
    }
    public SeatDto.addResponse createViewPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest request){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        ShinhanSeat seat = getSeat(seatId).get();
        List<Image> images = getImage(request.getImage());

        request.setUser(user);
        request.setTheater(theater);
//        request.setShinhanSeat(seat);
        request.setImage(images);

        ShinhanPost view = request.toShinhanPostEntity();
        view.setShinhanSeat(seat);

        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images){
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new SeatDto.addResponse(shinhanPostRepository.save(view));
    }
    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow();
    }
    private Theater getTheater(Long theaterId){
        return theaterRepository.findById(theaterId).orElseThrow();
    }
    private List<Image> getImage(List<Image> images){
        List<Image> tmp = imageRepository.findAll();
        for (Image a : tmp){
            for(Image i : images){
                if(a.getImgUrl().equals(i.getImgUrl()))
                    i.setImgUrl(a.getImgUrl());
            }
        }
        return images;
    }
    private Optional<ShinhanSeat> getSeat(Long seatId){
        return shinhanRepository.findById(seatId);
    }

}
