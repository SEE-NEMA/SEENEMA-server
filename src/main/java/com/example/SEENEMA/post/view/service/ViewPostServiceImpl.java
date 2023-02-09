package com.example.SEENEMA.post.view.service;

import com.example.SEENEMA.post.view.dto.ViewPostDto;
import com.example.SEENEMA.post.view.repository.ViewPostRepository;
import com.example.SEENEMA.post.view.domain.ViewPost;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.theater.repository.TheaterRepository;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ViewPostServiceImpl implements ViewPostService {
    private final ViewPostRepository viewRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;

    @Override
    @Transactional
    public ViewPostDto.addResponse createView(Long userId, Long theaterId, ViewPostDto.addRequest requestDto){

        User user = getUser(userId);
        Theater theater = getTheater(theaterId);

        requestDto.setUser(user);
        requestDto.setTheater(theater);

        ViewPost view = requestDto.toEntity();

        return new ViewPostDto.addResponse(viewRepository.save(view));

    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow();
    }

    private Theater getTheater(Long theaterId){
        return theaterRepository.findById(theaterId).orElseThrow();
    }
}
