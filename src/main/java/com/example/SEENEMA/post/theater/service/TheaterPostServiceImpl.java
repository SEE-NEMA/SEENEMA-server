package com.example.SEENEMA.post.theater.service;

import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.post.theater.repository.TheaterPostRepository;
import com.example.SEENEMA.tag.domain.Tag;
import com.example.SEENEMA.tag.repository.TagRepository;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.theater.repository.TheaterRepository;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TheaterPostServiceImpl implements TheaterPostService{
    private final TheaterPostRepository theaterPostRepo;
    private final UserRepository userRepo;
    private final TheaterRepository theaterRepo;
    private final TagRepository tagRepo;

    @Override
    @Transactional
    public TheaterPostDto.addResponse createTheaterPost(Long userId, TheaterPostDto.addRequest request){
        // 공연장 후기 게시글 작성
        User user = getUser(userId);

        String theaterName = findTheaterName(request.getTitle());
        Theater theater = getTheater(theaterName);

        List<Tag> tags = getTags(request.getTags());

        request.setUser(user);
        request.setTheater(theater);
        request.setTags(tags);

        TheaterPost theaterPost = request.toEntity();

        return new TheaterPostDto.addResponse(theaterPostRepo.save(theaterPost));
    }
    @Override
    @Transactional
    public List<TheaterPostDto.listResponse> listTheaterPost(){
        // 공연장 후기 게시글 메인 페이지
        List<TheaterPost> originPost = theaterPostRepo.findAll();
        List<TheaterPostDto.listResponse> result = new ArrayList<>();

        for(TheaterPost t : originPost){
            TheaterPostDto.listResponse tmp = new TheaterPostDto.listResponse(t);
            result.add(tmp);
        }
        return result;
    }

    @Override
    @Transactional
    public TheaterPostDto.deleteResponse deleteTheaterPost(Long postNo){
        // 공연장 후기 게시글 삭제
        theaterPostRepo.deleteById(postNo);
        return null;
    }

    @Override
    @Transactional
    public TheaterPostDto.addResponse editTheaterPost(Long userId, Long postNo, TheaterPostDto.addRequest request){
        User user = getUser(userId);
        String theaterName = findTheaterName(request.getTitle());
        Theater theater = getTheater(theaterName);
        List<Tag> tags = getTags(request.getTags());

        TheaterPost t = getTheaterPost(postNo);
        t.setEditedAt(LocalDateTime.now());
        t.setTheater(theater);
        t.setTags(tags);
        t.setTitle(request.getTitle());
        t.setContent(request.getContent());

        request.setUser(user);
        request.setTheater(theater);
        request.setTags(tags);


        return new TheaterPostDto.addResponse(theaterPostRepo.save(t));
    }

    private User getUser(Long userId){
        return userRepo.findById(userId).get();
    }
    private String findTheaterName(String origin){
        String[] title = origin.split("\\+");
        String[] title2 = title[0].split(" ", 2);
        String theaterName = title2[1];
        theaterName = theaterName.trim();
        return theaterName;
    }
    private Theater getTheater(Long theaterId){
        return theaterRepo.findById(theaterId).get();
    }
    private Theater getTheater(String theaterName){
        List<Theater> theaters = theaterRepo.findAll();
        for(Theater t:theaters){
            if(t.getTheaterName().equals(theaterName)) {
                return t;
            }
        }
        return null;
    }
    private TheaterPost getTheaterPost(Long post_no){
        List<TheaterPost> theaterPost = theaterPostRepo.findAll();
        for(TheaterPost t:theaterPost){
            if(t.getPostNo().equals(post_no)){
                return t;
            }
        }
        return null;
    }
    private List<Tag> getTags(List<Tag> tags){
        List<Tag> tmp = tagRepo.findAll();
        for (Tag t : tmp){
            for(Tag ex : tags){
                if(t.getTagId().equals(ex.getTagId()))
                    ex.setTagName(t.getTagName());
            }
        }
        return tags;
    }
}
