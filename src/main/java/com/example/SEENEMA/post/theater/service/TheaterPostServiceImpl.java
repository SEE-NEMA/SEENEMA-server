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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        User user = getUser(userId);
        //String theaterName = request.getTitle().split(" + ",2)[0];
        //title = "[ 충무아트센터 + 그날들 ] 공연 후기 !~!"

        String[] title = request.getTitle().split("\\+");
        String[] title2 = title[0].split(" ",2);
        String theaterName = title2[1];
        theaterName=theaterName.trim();
        Theater theater = getTheater(theaterName);

        List<Tag> tags = getTags(request.getTags());

        request.setUser(user);
        request.setTheater(theater);
        request.setTags(tags);

        TheaterPost theaterPost = request.toEntity();

        return new TheaterPostDto.addResponse(theaterPostRepo.save(theaterPost));
    }

    private User getUser(Long userId){
        return userRepo.findById(userId).get();
    }
    private Theater getTheater(Long theaterId){
        return theaterRepo.findById(theaterId).get();
    }
    private Theater getTheater(String theaterName){
        List<Theater> theaters = theaterRepo.findAll();
        for(Theater t:theaters){
            if(t.getTheaterName().toString().equals(theaterName.toString())) {
                //log.info(t.getTheaterName().toString());
                return t;
            }
        }
        return null;
    }
    private List<Tag> getTags(List<Tag> tags){
        List<Tag> tmp = tagRepo.findAll();
        int index=0;
        for (Tag t : tmp){
            for(Tag ex : tags){
                if(t.getTagId().equals(ex.getTagId()))
                    ex.setTagName(t.getTagName());
            }
        }
        return tags;
    }
}
