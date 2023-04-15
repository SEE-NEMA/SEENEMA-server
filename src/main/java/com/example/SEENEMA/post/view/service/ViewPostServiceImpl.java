package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.view.domain.Image;
import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.view.repository.ImageRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ViewPostServiceImpl implements ViewPostService {
    private final ViewPostRepository viewPostRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final ImageRepository imageRepository;

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
    public ViewPostDto.detailResponse readViewPost(Long userId, Long theaterId, Long viewNo){

        ViewPost viewPost = getViewPost(theaterId,viewNo);
        // 이미지 컬렉션을 명시적으로 초기화
        Hibernate.initialize(viewPost.getImage());
        return new ViewPostDto.detailResponse(viewPost);
    }

    @Override
    @Transactional
    public ViewPostDto.addResponse updateViewPost(Long theaterId,Long viewNo, ViewPostDto.updateRequest requestDto){

        ViewPost viewPost = getViewPost(theaterId,viewNo);
        viewPost.updateViewPost(requestDto.getPlay(), requestDto.getSeat(), requestDto.getTitle(), requestDto.getContent(), requestDto.getImage());

        return new ViewPostDto.addResponse(viewPost);
    }

    @Override
    @Transactional
    public void deleteViewPost(Long theaterId, Long viewNo){

        ViewPost viewPost = getViewPost(theaterId, viewNo);
        viewPostRepository.delete(viewPost);
    }

    @Override
    public List<ViewPostDto.viewListResponse> getListByTheater(Long theaterId){

        return viewPostRepository.findByTheater_TheaterId(theaterId).stream()
                .map(ViewPostDto.viewListResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ViewPostDto.viewListResponse> getListBySeat(Long theaterId, String seatName){

        return viewPostRepository.findByTheater_TheaterIdAndTitleContaining(theaterId,seatName).stream()
                .map(ViewPostDto.viewListResponse::new)
                .collect(Collectors.toList());
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
}
