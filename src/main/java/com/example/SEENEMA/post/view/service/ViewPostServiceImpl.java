package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.view.domain.ViewPostHeart;
import com.example.SEENEMA.post.view.dto.ResponseMessage;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.file.ImageRepository;
import com.example.SEENEMA.post.view.repository.ViewPostHeartRepository;
import com.example.SEENEMA.post.view.repository.ViewPostRepository;
import com.example.SEENEMA.post.view.domain.ViewPost;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ViewPostServiceImpl implements ViewPostService {
    private final ViewPostRepository viewPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;
    private final ViewPostHeartRepository heartRepository;
    @Override
    @Transactional
    public ViewPostDto.addResponse createViewPost(Long userId, Long theaterId, ViewPostDto.addRequest requestDto){
        User user = getUser(userId);
        Theater theater = getTheater(theaterId);
        List<Image> images = getImage(requestDto.getImage());

        requestDto.setUser(user);
        requestDto.setTheater(theater);
        requestDto.setImage(images);

        ViewPost view = requestDto.toEntity();

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        view.setImage(persistedImages);
        return new ViewPostDto.addResponse(viewPostRepository.save(view));
    }


    @Override
    @Transactional(readOnly = true)
    public ViewPostDto.detailResponse readViewPost(Long theaterId, Long viewNo){

        ViewPost viewPost = getViewPost(theaterId,viewNo);
        viewPost.setHeartCount((long) heartRepository.findByViewPost(viewPost).size());
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(viewPost.getImage());
        return new ViewPostDto.detailResponse(viewPost);
    }
    @Override
    @Transactional
    public ViewPostDto.detailResponse readViewPost(Long theaterId, Long viewNo, Long userId){
        // 로그인 한 사용자가 게시글을 조회하는 경우 -> 좋아요 여부 판단 필요
        User u = getUser(userId);
        ViewPost v = getViewPost(theaterId, viewNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 detailResponse의 heartedYN
        ViewPostHeart tmp = heartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            ViewPostDto.detailResponse response = readViewPost(theaterId, viewNo);
            response.setHeartedYN(Boolean.TRUE);
            return  response;
        }
        else return readViewPost(theaterId, viewNo);
    }

    @Override
    @Transactional
    public String authUserForEdit(Long theaterId, Long viewNo, Long userId){
        ViewPost viewPost = getViewPost(theaterId, viewNo);
        if(viewPost.getUser().getUserId().equals(userId)) return "SUCCESS";
        else return "NOT_SAME_USER";
    }

    @Override
    @Transactional
    public ViewPostDto.addResponse updateViewPost(Long theaterId,Long viewNo, ViewPostDto.updateRequest requestDto, Long userId){
        ViewPost viewPost = getViewPost(theaterId,viewNo);
        // update전 작성자와 사용자 동일인 판별
        if(!viewPost.getUser().getUserId().equals(userId)) {
            // 동일인 X -> 수정 X
            return new ViewPostDto.addResponse(viewPost);
        }
        else{
            viewPost.updateViewPost(requestDto.getPlay(), requestDto.getSeat(), requestDto.getTitle(), requestDto.getContent(),
                    requestDto.getViewScore(), requestDto.getSeatScore(), requestDto.getLightScore(), requestDto.getSoundScore(), requestDto.getImage());
            return new ViewPostDto.addResponse(viewPost);
        }
    }

    @Override
    @Transactional
    public String deleteViewPost(Long theaterId, Long viewNo, Long userId){
        // 시야 후기 게시글 삭제
        ViewPost viewPost = getViewPost(theaterId, viewNo);
        if(viewPost.getUser().getUserId().equals(userId)) {
            deleteHeartByViewNo(theaterId, viewNo);
            viewPostRepository.delete(viewPost);
            return ResponseMessage.DELETE.getMsg();
        }
        else {
            return "FAIL";
        }
    }
    @Override
    @Transactional
    public ViewPostDto.detailResponse heartViewPost(Long theaterId, Long viewNo, Long userId){
        // 게시글 좋아요
        User u = getUser(userId);
        ViewPost v = getViewPost(theaterId, viewNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 무시
        ViewPostHeart tmp = heartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            return readViewPost(theaterId, viewNo, userId);
        }

        ViewPostHeart heart = ViewPostHeart.builder()
                .viewPost(v)
                .user(u)
                .build();
        heartRepository.save(heart);    // 사용자와 게시글 좋아요 정보 저장
        v.setHeartCount(v.getHeartCount() + 1L);    // 좋아요 갯수 + 1
        viewPostRepository.save(v);
        return readViewPost(theaterId, viewNo, userId);
    }
    @Override
    @Transactional
    public ViewPostDto.detailResponse cancelHeart(Long theaterId, Long viewNo, Long userId){
        User u = getUser(userId);
        ViewPost v = getViewPost(theaterId, viewNo);
        // 좋아요 취소
        ViewPostHeart tmp = heartRepository.findByUserAndViewPost(u, v);
        if(tmp != null){
            System.out.println(tmp.getUser().getNickname()+tmp.getViewPost().getTitle());
            heartRepository.delete(tmp);
        }
        v.setHeartCount(v.getHeartCount() - 1L);
        viewPostRepository.save(v);
        return readViewPost(theaterId, viewNo, userId);
    }

    @Override
    public List<ViewPostDto.viewListResponse> getListByTheater(Long theaterId){
        List<ViewPostDto.viewListResponse> response = viewPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(ViewPostDto.viewListResponse::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<ViewPostDto.viewListResponse> getListBySeat(Long theaterId, String seatName){
        List<ViewPostDto.viewListResponse> response = viewPostRepository.findByTheater_TheaterIdAndTitleContaining(theaterId,seatName).stream()
                .map(ViewPostDto.viewListResponse::new)
                .collect(Collectors.toList());
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    private ViewPost getViewPost(Long theaterId, Long viewNo) { return viewPostRepository.findByTheater_TheaterIdAndViewNo(theaterId,viewNo); }

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

    private void deleteHeartByViewNo(Long theaterId, Long viewNo){
        List<ViewPostHeart> tmp = heartRepository.findAll();
        for (ViewPostHeart h : tmp) {
            if (h.getViewPost() == getViewPost(theaterId, viewNo)) heartRepository.delete(h);
        }
    }
}
