package com.example.SEENEMA.domain.seat.blueSquareShinhan;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.post.view.dto.ResponseMessage;
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
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public SeatDto.addResponse createSeatPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest request){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        ShinhanSeat seat = getSeat(seatId).get();
        List<Image> images = getImage(request.getImage());

        request.setUser(user);
        request.setTheater(theater);
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
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.addResponse(view);
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
            for(SeatDto.seatViewList s : seatViewLists) {
                System.out.println(s.getViewNo()+s.getAverage());
                avr += s.getAverage();
            }
            avr /= seatViewLists.size();
            response.setAverage(avr);
            return response;
        }
    }

    public List<SeatDto.seatViewList> getListByTheater(Long theaterId){
        // 공연장별 게시글 조회
        List<SeatDto.seatViewList> response = shinhanPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    public List<SeatDto.seatAverage> getAverageList(Long theaterId) {
        // 공연장 전체 좌석 평균 목록
        List<ShinhanPost> posts = shinhanPostRepository.findByTheater_TheaterId(theaterId);
        Map<Long, List<ShinhanPost>> seatPostMap = new HashMap<>();
        List<SeatDto.seatAverage> seatAverages = new ArrayList<>();

        // 좌석별 게시물 그룹화
        for(ShinhanPost post : posts){
            Long seatId = post.getShinhanSeat().getSeatId();
            if(seatPostMap.containsKey(seatId)){
                seatPostMap.get(seatId).add(post);
            }
            else{
                List<ShinhanPost> postList = new ArrayList<>();
                postList.add(post);
                seatPostMap.put(seatId, postList);
            }
        }

        // 좌석별 평균 계산
        for(Map.Entry<Long, List<ShinhanPost>> entry : seatPostMap.entrySet()){
            Long seatId = entry.getKey();
            List<ShinhanPost> postList = entry.getValue();

            SeatDto.seatAverage seatAverage = new SeatDto.seatAverage();
            ShinhanSeat seat = shinhanRepository.findById(seatId).orElse(null);

            if(seat != null){
                seatAverage.setX(seat.getX());
                seatAverage.setY(seat.getY());
                seatAverage.setZ(seat.getZ());
            }
            if(postList.isEmpty()){
                seatAverage.setPostedYN(Boolean.FALSE);
                seatAverage.setAverage(0);
            }
            else{
                seatAverage.setPostedYN(Boolean.TRUE);
                int totalScore = 0;
                for(ShinhanPost p : postList)
                    totalScore += (p.getViewScore() + p.getSoundScore() + p.getLightScore() + p.getSeatScore());
                int average = totalScore / (postList.size() * 4);
                seatAverage.setAverage(average);
            }
            seatAverages.add(seatAverage);
        }

        return seatAverages;
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
}