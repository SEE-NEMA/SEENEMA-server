package com.example.SEENEMA.domain.seat.chungmu;


import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.post.view.dto.ResponseMessage;
import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuPost;
import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuSeat;
import com.example.SEENEMA.domain.seat.chungmu.repository.ChungmuPostRepository;
import com.example.SEENEMA.domain.seat.chungmu.repository.ChungmuRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class ChungmuService {

    private final ChungmuRepository chungmuRepository;
    private final ChungmuPostRepository chungmuPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;

    public static String convertToSeatNumber(int x, int y, int z) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용
        String floor = String.valueOf(z); // 층은 z 좌표 그대로 사용

        return floor + "층 " + column + "열 " + number + "번";
    }

    @Transactional
    public SeatDto.addResponse createSeatPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest requestDto){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        ChungmuSeat chungmuSeat = getSeat(seatId);
        List<Image> images = getImage(requestDto.getImage());

        requestDto.setUser(user);
        requestDto.setTheater(theater);
        requestDto.setImage(images);

        ChungmuPost view = requestDto.toChungmuPostEntity();
        view.setChungmuSeat(chungmuSeat); // requestDto에 Seat 정보 지워서 설정 따로 필요

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new SeatDto.addResponse(chungmuPostRepository.save(view));
    }

    @Transactional(readOnly = true)
    public SeatDto.addResponse readSeatPost(Long theaterId, Long seatId, Long viewNo){
        ChungmuPost view = getSeatPost(theaterId,seatId, viewNo);

        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.addResponse(view);
    }


    @Transactional
    public String authUserForEdit(Long theaterId, Long seatId, Long viewNo, Long userId){
        ChungmuPost chungmuPost = getSeatPost(theaterId,seatId, viewNo);
        if(chungmuPost.getUser().getUserId().equals(userId)) return "SUCCESS";
        else return "NOT_SAME_USER";
    }

    @Transactional
    public SeatDto.addResponse updateSeatPost(Long theaterId,Long seatId, Long viewNo, SeatDto.updateRequest requestDto, Long userId){
        ChungmuPost seatPost = getSeatPost(theaterId,seatId, viewNo);
        // update전 작성자와 사용자 동일인 판별
        if(!seatPost.getUser().getUserId().equals(userId)) {
            // 동일인 X -> 수정 X
            return new SeatDto.addResponse(seatPost);
        }
        else{
            seatPost.updateSeatPost(requestDto.getPlay(), requestDto.getTitle(), requestDto.getContent(),
                    requestDto.getViewScore(), requestDto.getSeatScore(), requestDto.getLightScore(), requestDto.getSoundScore(), requestDto.getImage());
            return new SeatDto.addResponse(seatPost);
        }
    }

    @Transactional
    public String deleteSeatPost(Long theaterId, Long seatId, Long viewNo, Long userId){
        // 시야 후기 게시글 삭제
        ChungmuPost seatPost = getSeatPost(theaterId, seatId, viewNo);
        if(seatPost.getUser().getUserId().equals(userId)) {
            chungmuPostRepository.delete(seatPost);
            return ResponseMessage.DELETE.getMsg();
        }
        else {
            return "FAIL";
        }
    }


    public List<SeatDto.seatAverage> getAverageList(Long theaterId) {
        List<ChungmuPost> posts = chungmuPostRepository.findByTheater_TheaterId(theaterId);
        Map<Long, List<ChungmuPost>> seatPostMap = new HashMap<>();
        List<SeatDto.seatAverage> seatAverages = new ArrayList<>();

        // 좌석별 게시물 그룹화
        for (ChungmuPost post : posts) {
            Long seatId = post.getChungmuSeat().getSeatId();
            if (seatPostMap.containsKey(seatId)) {
                seatPostMap.get(seatId).add(post);
            } else {
                List<ChungmuPost> postList = new ArrayList<>();
                postList.add(post);
                seatPostMap.put(seatId, postList);
            }
        }

        // 좌석별 평균 계산
        for (Map.Entry<Long, List<ChungmuPost>> entry : seatPostMap.entrySet()) {
            Long seatId = entry.getKey();
            List<ChungmuPost> postList = entry.getValue();

            SeatDto.seatAverage seatAverage = new SeatDto.seatAverage();
            ChungmuSeat seat = chungmuRepository.findById(seatId).orElse(null);

            if (seat != null) {
                seatAverage.setX(seat.getX());
                seatAverage.setY(seat.getY());
                seatAverage.setZ(seat.getZ());
            }

            if (postList.isEmpty()) {
                seatAverage.setPostedYN(false);
                seatAverage.setAverage(0);
            } else {
                seatAverage.setPostedYN(true);
                int totalScore = 0;
                for (ChungmuPost post : postList) {
                    totalScore += (post.getViewScore() + post.getSeatScore() + post.getLightScore() + post.getSoundScore());
                }
                int average = totalScore / (postList.size() * 4);
                seatAverage.setAverage(average);
            }
            seatAverages.add(seatAverage);
        }

        return seatAverages;
    }



    public SeatDto.postList getListBySeat(Long theaterId, Long seatId){
        List<SeatDto.seatViewList> seatViewLists = chungmuPostRepository.findByTheater_TheaterIdAndChungmuSeat_SeatId(theaterId,seatId).stream()
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

    public List<SeatDto.seatViewList> getListByTheater(Long theaterId){
        List<SeatDto.seatViewList> response = chungmuPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }
    private ChungmuPost getSeatPost(Long theaterId, Long seatId, Long viewNo) {
        return chungmuPostRepository.findByTheater_TheaterIdAndChungmuSeat_SeatIdAndViewNo(theaterId,seatId,viewNo);
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
    private ChungmuSeat getSeat(Long seatId){
        return chungmuRepository.findById(seatId).orElseThrow();
    }
}
