package com.example.SEENEMA.domain.seat.blueSquareShinhan;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.post.view.dto.ResponseMessage;
import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanHeart;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanSeat;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanHeartRepository;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanPostRepository;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.readers.operation.ResponseMessagesReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShinhanService {
    private final ShinhanRepository shinhanRepository;
    private final ShinhanPostRepository shinhanPostRepository;
    private final ShinhanHeartRepository shinhanHeartRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;
    public static String convertToSeatNumber(int x, int y, int z) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용
        String floor = String.valueOf(z); // 층은 z 좌표 그대로 사용

        return floor + "층 " + column + "열 " + number + "번";
    }
    public SeatDto.addResponse createSeatPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest request){
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
    @Transactional(readOnly = true)
    public SeatDto.addResponse readSeatPost(Long theaterId, Long seatId, Long viewNo){
        ShinhanPost view = shinhanPostRepository.findById(viewNo).get();
        view.setHeartCount((long) shinhanHeartRepository.findByViewPost(view).size());
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.addResponse(view);
    }
    @Transactional(readOnly = true)
    public SeatDto.addResponse readSeatPost(Long theaterId, Long seatId, Long viewNo, Long userId){
        // 로그인한 사용자가 게시글 조회하는 경우 -> 좋아요 여부 판단 필요
        User u = getUser(userId);
        ShinhanPost v = getSeatPost(viewNo);
        ShinhanHeart tmp = shinhanHeartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            SeatDto.addResponse response = readSeatPost(theaterId, seatId, viewNo);
            response.setHeartedYN(Boolean.TRUE);
            return response;
        }
        return readSeatPost(theaterId, seatId, viewNo);
    }

    @Transactional
    public String authUserForEdit(Long theaterId, Long seatId, Long viewNo, Long userId){
        ShinhanPost shinhanPost = getSeatPost(viewNo);
        if(shinhanPost.getUser().getUserId().equals(userId)) return "SUCCESS";
        return "NOT_SAME_USER";
    }

    @Transactional
    public SeatDto.addResponse updateSeatPost(Long theaterId, Long sesatId, Long viewNo, SeatDto.updateRequest request, Long userId){
        ShinhanPost seatPost = getSeatPost(viewNo);
        // update 전 작성자와 사용자 동일 판별
        if(!seatPost.getUser().getUserId().equals(userId)) return new SeatDto.addResponse(seatPost);
        else{
            seatPost.updateSeatPost(request.getPlay(), request.getTitle(), request.getContent(),
                    request.getViewScore(), request.getSeatScore(), request.getLightScore(), request.getSoundScore(),
                    request.getImage());
            return new SeatDto.addResponse(seatPost);
        }
    }
    @Transactional
    public String deleteSeatPost(Long theaterId, Long seatId, Long viewNo, Long userId){
        // 삭제
        ShinhanPost seatPost = getSeatPost(viewNo);
        if(seatPost.getUser().getUserId().equals(userId)){
            deleteHeartByViewNo(theaterId, seatId, viewNo);
            shinhanPostRepository.delete(seatPost);
            return ResponseMessage.DELETE.getMsg();
        }
        else {
            return "FAIL";
        }
    }

    public SeatDto.postList getListBySeat(Long theaterId, Long seatId){
        List<SeatDto.seatViewList> seatViewLists = shinhanPostRepository.findByTheater_TheaterIdAndShinhanSeat_SeatId(theaterId, seatId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(seatViewLists, Collections.reverseOrder());
        SeatDto.postList response = new SeatDto.postList(seatViewLists);
        if(seatViewLists.isEmpty()){
            response.setPostedYN(Boolean.TRUE);
            response.setAverage(0);
            return response;
        }
        else{
            response.setPostedYN(Boolean.TRUE);
            int avr = 0;
            for(SeatDto.seatViewList s : seatViewLists)
                avr += s.getAverage();
            avr /= seatViewLists.size();
            response.setAverage(avr);
            return response;
        }
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
    private ShinhanPost getSeatPost(Long viewNo){
        return shinhanPostRepository.findById(viewNo).get();
    }
    private void deleteHeartByViewNo(Long theaterId, Long seatId, Long viewNo){
        List<ShinhanHeart> tmp = shinhanHeartRepository.findAll();
        for(ShinhanHeart h : tmp)
            if(h.getViewPost() == getSeatPost(viewNo)) shinhanHeartRepository.delete(h);
    }
}