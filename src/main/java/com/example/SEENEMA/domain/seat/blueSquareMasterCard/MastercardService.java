package com.example.SEENEMA.domain.seat.blueSquareMasterCard;

import com.example.SEENEMA.domain.post.file.Image;
import com.example.SEENEMA.domain.post.file.ImageRepository;
import com.example.SEENEMA.domain.post.view.dto.ResponseMessage;
import com.example.SEENEMA.domain.seat.SeatDto;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardPost;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardSeat;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.repository.MastercardPostRepository;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.repository.MastercardRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.user.domain.Reward;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.user.repository.RewardRepository;
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
public class MastercardService {
    private final MastercardRepository seatRepo;
    private final MastercardPostRepository postRepo;
    private final UserRepository userRepo;
    private final TheaterRepository theaterRepo;
    private final ImageRepository imageRepo;
    private final RewardRepository rewardRepo;
    public static String convertToSeatNumber(int x, int y, int z) {
        String column = String.valueOf(x);  // 열은 x 좌표 그대로 사용
        String number = String.valueOf(y);  // 번호는 y 좌표 그대로 사용
        String floor = String.valueOf(z); // 층은 z 좌표 그대로 사용
        if(x==1){
            return floor + "층 B구역 " + number + "번";
        }
        else {
            return floor + "층 " + column + "열 " + number + "번";
        }
    }
    public SeatDto.addResponse createSeatPost(Long userId, Long theaterId, Long seatId, SeatDto.addRequest request){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        MastercardSeat seat = getSeat(seatId);
        List<Image> images = getImage(request.getImage());

        request.setUser(user);
        request.setTheater(theater);
        request.setImage(images);

        MastercardPost view = request.toMastercardPostEntity();
        view.setMastercardSeat(seat);

        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images)
            persistedImages.add(imageRepo.save(image));
        view.setImage(persistedImages);

        // 리워드 지급
        Reward reward = rewardRepo.findByUser(user);
        reward.setPoint(reward.getPoint() + 100L);
        rewardRepo.save(reward);

        return new SeatDto.addResponse(postRepo.save(view));
    }
    @Transactional(readOnly = true)
    public SeatDto.addResponse readSeatPost(Long userId, Long theaterId, Long seatId, Long viewNo){
        MastercardPost view = postRepo.findByViewNo(viewNo);
        // 리워드 차감
        User user = getUser(userId);

        // 게시글 작성자와 조회하는 사용자가 다를 경우에만 리워드 차감
        if (!view.getUser().getUserId().equals(user.getUserId())) {
            Reward reward = rewardRepo.findByUser(user);
            reward.setPoint(reward.getPoint() - 10L);
            rewardRepo.save(reward);
        }

        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(view.getImage());
        return new SeatDto.addResponse(view);
    }
    @Transactional
    public String authUserForEdit(Long theaterId, Long seatId, Long viewNo, Long userId){
        MastercardPost mastercardPost = getSeatPost(viewNo);
        if(mastercardPost.getUser().getUserId().equals(userId)) return "SUCCESS";
        return "NOT_SAME_USER";
    }
    @Transactional
    public SeatDto.addResponse updateSeatPost(Long theaterId, Long seatId, Long viewNo, SeatDto.updateRequest request, Long userId){
        MastercardPost seatPost = getSeatPost(viewNo);
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
        MastercardPost seatPost = getSeatPost(viewNo);
        if(seatPost.getUser().getUserId().equals(userId)){
            postRepo.delete(seatPost);
            return ResponseMessage.DELETE.getMsg();
        }
        else return "FAIL";
    }

    public SeatDto.postList getListBySeat(Long theaterId, Long seatId){
        List<SeatDto.seatViewList> seatViewLists = postRepo.findByTheater_TheaterIdAndMastercardSeat_SeatId(theaterId, seatId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(seatViewLists, Collections.reverseOrder());
        SeatDto.postList response = new SeatDto.postList(seatViewLists);
        if(seatViewLists.isEmpty()){
            response.setPostedYN(Boolean.FALSE);
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
            return  response;
        }
    }
    public List<SeatDto.seatViewList> getListByTheater(Long theaterId){
        // 공연장별 게시글 조회
        List<SeatDto.seatViewList> response = postRepo.findByTheater_TheaterId(theaterId).stream()
                .map(SeatDto.seatViewList::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    public List<SeatDto.seatAverage> getAverageList(Long theaterId){
        // 공연장 전체 좌석 평균 목록
        List<MastercardPost> posts = postRepo.findByTheater_TheaterId(theaterId);
        Map<Long, List<MastercardPost>> seatPostMap = new HashMap<>();
        List<SeatDto.seatAverage> seatAverages = new ArrayList<>();

        // 좌석별 게시물 그룹화
        for(MastercardPost post : posts){
            Long seatId = post.getMastercardSeat().getSeatId();
            if(seatPostMap.containsKey(seatId)) seatPostMap.get(seatId).add(post);
            else{
                List<MastercardPost> postList = new ArrayList<>();
                postList.add(post);
                seatPostMap.put(seatId, postList);
            }
        }
        // 좌석별 평균 계산
        for(Map.Entry<Long, List<MastercardPost>> entry : seatPostMap.entrySet()){
            Long seatId = entry.getKey();
            List<MastercardPost> postList = entry.getValue();

            SeatDto.seatAverage seatAverage = new SeatDto.seatAverage();
            MastercardSeat seat = seatRepo.findById(seatId).orElse(null);

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
                for(MastercardPost p : postList)
                    totalScore += (p.getViewScore() + p.getSoundScore() + p.getLightScore() + p.getSeatScore());
                int average = totalScore / (postList.size() * 4);
                seatAverage.setAverage(average);
            }
            seatAverages.add(seatAverage);
        }
        return seatAverages;
    }
    private User getUser(Long userId){
        return userRepo.findById(userId).orElseThrow();
    }
    private Theater getTheater(Long theaterId){
        return theaterRepo.findById(theaterId).orElseThrow();
    }
    private List<Image> getImage(List<Image> images){
        List<Image> tmp = imageRepo.findAll();
        for (Image a : tmp){
            for(Image i : images){
                if(a.getImgUrl().equals(i.getImgUrl()))
                    i.setImgUrl(a.getImgUrl());
            }
        }
        return images;
    }
    private MastercardSeat getSeat(Long seatId){
        return seatRepo.findById(seatId).orElseThrow();
    }
    private MastercardPost getSeatPost(Long viewNo){
        return postRepo.findByViewNo(viewNo);
    }
}
