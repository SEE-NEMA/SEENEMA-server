package com.example.SEENEMA.domain.seat.arcoTheater;

import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoHeart;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoHeartRepository;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoPostRepository;
import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoSeat;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoRepository;
import com.example.SEENEMA.domain.post.view.repository.ViewPostHeartRepository;
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
    private final ViewPostHeartRepository heartRepository;
    private final ArcoHeartRepository arcoHeartRepository;

    public static String convertToSeatNumber(int x, int y) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용

        return column + "열 " + number + "번";
    }

    @Transactional
    public ArcoDto.addResponse createViewPost(Long userId, Long theaterId, Long seatId, ArcoDto.addRequest requestDto){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        ArcoSeat arcoSeat = getSeat(seatId);
        List<Image> images = getImage(requestDto.getImage());

        requestDto.setUser(user);
        requestDto.setTheater(theater);
        requestDto.setArcoSeat(arcoSeat);
        requestDto.setImage(images);

        ArcoPost view = requestDto.toEntity();

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new ArcoDto.addResponse(arcoPostRepository.save(view));
    }

    @Transactional(readOnly = true)
    public ArcoDto.detailResponse readViewPost(Long theaterId, Long viewNo){

        ArcoPost view = getSeatViewPost(theaterId,viewNo);
        view.setHeartCount((long) arcoHeartRepository.findByViewPost(view).size());
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new ArcoDto.detailResponse(view);
    }

    @Transactional
    public ArcoDto.detailResponse readViewPost(Long theaterId, Long viewNo, Long userId){
        // 로그인 한 사용자가 게시글을 조회하는 경우 -> 좋아요 여부 판단 필요
        User u = getUser(userId);
        ArcoPost v = getSeatViewPost(theaterId, viewNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 detailResponse의 heartedYN
        ArcoHeart tmp = arcoHeartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            ArcoDto.detailResponse response = readViewPost(theaterId, viewNo);
            response.setHeartedYN(Boolean.TRUE);
            return  response;
        }
        return readViewPost(theaterId, viewNo);
    }


    public List<ArcoDto.seatViewList> getListByTheater(Long theaterId){
        List<ArcoDto.seatViewList> response = arcoPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(ArcoDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }


    public List<ArcoDto.seatViewList> getListBySeat(Long theaterId, String seatName){
        List<ArcoDto.seatViewList> response = arcoPostRepository.findByTheater_TheaterIdAndTitleContaining(theaterId,seatName).stream()
                .map(ArcoDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    private ArcoPost getSeatViewPost(Long theaterId, Long viewNo) { return arcoPostRepository.findByTheater_TheaterIdAndViewNo(theaterId,viewNo); }

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
