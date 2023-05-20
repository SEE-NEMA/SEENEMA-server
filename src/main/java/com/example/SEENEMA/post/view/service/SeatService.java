package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.file.ImageRepository;
import com.example.SEENEMA.post.view.domain.Seat;
import com.example.SEENEMA.post.view.domain.SeatViewPost;
import com.example.SEENEMA.post.view.dto.SeatDto;
import com.example.SEENEMA.post.view.repository.SeatRepository;
import com.example.SEENEMA.post.view.repository.SeatViewPostRepository;
import com.example.SEENEMA.post.view.repository.ViewPostHeartRepository;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.theater.repository.TheaterRepository;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final SeatViewPostRepository seatViewPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;
    private final ViewPostHeartRepository heartRepository;

    public static String convertToSeatNumber(int x, int y) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용

        return column + "열 " + number + "번";
    }

    @Transactional
    public SeatDto.addResponse createViewPost(Long userId, Long theaterId,Long seatId, SeatDto.addRequest requestDto){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        Seat seat = getSeat(seatId);
        List<Image> images = getImage(requestDto.getImage());

        requestDto.setUser(user);
        requestDto.setTheater(theater);
        requestDto.setSeat(seat);
        requestDto.setImage(images);

        SeatViewPost view = requestDto.toEntity();

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new SeatDto.addResponse(seatViewPostRepository.save(view));
    }

    @Transactional(readOnly = true)
    public SeatDto.detailResponse readViewPost(Long theaterId, Long viewNo){

        SeatViewPost view = getSeatViewPost(theaterId,viewNo);
        //viewPost.setHeartCount((long) heartRepository.findByViewPost(viewPost).size());
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.detailResponse(view);
    }

    @Transactional
    public SeatDto.detailResponse readViewPost(Long theaterId, Long viewNo, Long userId){
        // 로그인 한 사용자가 게시글을 조회하는 경우 -> 좋아요 여부 판단 필요
        User u = getUser(userId);
        SeatViewPost v = getSeatViewPost(theaterId, viewNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 detailResponse의 heartedYN
        //ViewPostHeart tmp = heartRepository.findByUserAndViewPost(u, v);
//        if(tmp != null){
//            SeatDto.detailResponse response = readViewPost(theaterId, viewNo);
//            response.setHeartedYN(Boolean.TRUE);
//            return  response;
//        }
        return readViewPost(theaterId, viewNo);
    }


    public List<SeatDto.seatViewList> getListByTheater(Long theaterId){
        List<SeatDto.seatViewList> response = seatViewPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }


    public List<SeatDto.seatViewList> getListBySeat(Long theaterId, String seatName){
        List<SeatDto.seatViewList> response = seatViewPostRepository.findByTheater_TheaterIdAndTitleContaining(theaterId,seatName).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    private SeatViewPost getSeatViewPost(Long theaterId, Long viewNo) { return seatViewPostRepository.findByTheater_TheaterIdAndViewNo(theaterId,viewNo); }

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
    private Seat getSeat(Long seatId){
        return seatRepository.findById(seatId).orElseThrow();
    }

}
