package com.example.SEENEMA.domain.seat.arcoTheater;

import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoHeart;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoHeartRepository;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoPostRepository;
import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
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
public class ArcoService {

    private final ArcoRepository arcoRepository;
    private final ArcoPostRepository arcoPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;
    private final ArcoHeartRepository arcoHeartRepository;

    public static String convertToSeatNumber(int x, int y, int z) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용
        String floor = String.valueOf(z); // 층은 z 좌표 그대로 사용

        return floor + "층 " + column + "열 " + number + "번";
    }

    @Transactional
    public SeatDto.addResponse createViewPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest requestDto){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        ArcoSeat arcoSeat = getSeat(seatId);
        List<Image> images = getImage(requestDto.getImage());

        requestDto.setUser(user);
        requestDto.setTheater(theater);
//        requestDto.setArcoSeat(arcoSeat);
        requestDto.setImage(images);

        ArcoPost view = requestDto.toArcoPostEntity();
        view.setArcoSeat(arcoSeat); // requestDto에 Seat 정보 지워서 설정 따로 필요

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new SeatDto.addResponse(arcoPostRepository.save(view));
    }

    @Transactional(readOnly = true)
    public SeatDto.detailResponse readViewPost(Long theaterId, Long seatId, Long viewNo){

        ArcoPost view = getSeatViewPost(theaterId,seatId, viewNo);

        view.setHeartCount((long) arcoHeartRepository.findByViewPost(view).size());
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.detailResponse(view);
    }

    @Transactional
    public SeatDto.detailResponse readViewPost(Long theaterId, Long seatId, Long viewNo, Long userId){
        // 로그인 한 사용자가 게시글을 조회하는 경우 -> 좋아요 여부 판단 필요
        User u = getUser(userId);
        ArcoPost v = getSeatViewPost(theaterId,seatId, viewNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 detailResponse의 heartedYN
        ArcoHeart tmp = arcoHeartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            SeatDto.detailResponse response = readViewPost(theaterId, seatId, viewNo);
            response.setHeartedYN(Boolean.TRUE);
            return  response;
        }
        return readViewPost(theaterId, seatId, viewNo);
    }

    public SeatDto.postList getListBySeat(Long theaterId, Long seatId){
        List<SeatDto.seatViewList> seatViewLists = arcoPostRepository.findByTheater_TheaterIdAndArcoSeat_SeatId(theaterId,seatId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(seatViewLists, Collections.reverseOrder());
        SeatDto.postList response = new SeatDto.postList(seatViewLists);
        if(seatViewLists.isEmpty()) {
            response.setPostedYN(Boolean.FALSE);
            response.setAverage(0);
            return response;
        }
        else {
            response.setPostedYN(Boolean.TRUE);
            int avr = 0;
            for(SeatDto.seatViewList s : seatViewLists){
                avr += s.getAverage();
            }
            avr/=seatViewLists.size();
            response.setAverage(avr);
            return response;
        }
    }

    private ArcoPost getSeatViewPost(Long theaterId, Long seatId, Long viewNo) {
        return arcoPostRepository.findByTheater_TheaterIdAndArcoSeat_SeatIdAndViewNo(theaterId,seatId,viewNo);
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
    private ArcoSeat getSeat(Long seatId){
        return arcoRepository.findById(seatId).orElseThrow();
    }

}
