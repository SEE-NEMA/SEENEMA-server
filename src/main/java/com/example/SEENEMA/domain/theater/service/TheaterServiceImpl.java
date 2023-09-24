package com.example.SEENEMA.domain.theater.service;

import com.example.SEENEMA.domain.theater.domain.TheaterImage;
import com.example.SEENEMA.domain.theater.repository.TheaterImageRepository;
import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.dto.TheaterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService{

    private final TheaterRepository theaterRepository;
    private final TheaterImageRepository imageRepository;
    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto.theaterResponse> searchTheater(String theaterName){
        return theaterRepository.findByTheaterNameContaining(theaterName).stream()
                .map(TheaterDto.theaterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheaterDto.theaterList> getTheater() {
        List<Theater> theaters = theaterRepository.findAll();
        List<TheaterDto.theaterList> theaterLists = new ArrayList<>();

        for (Theater theater : theaters) {
            TheaterDto.theaterList theaterList = new TheaterDto.theaterList(theater);

            // Find image for the theater using theaterId
            TheaterImage image = imageRepository.findByTheaterTheaterId(theater.getTheaterId());

            // Check if image is null
            String imgUrl = (image != null) ? image.getImgUrl() : null;

            // Set imgUrl in theaterList
            theaterList.setImgUrl(imgUrl);

            theaterLists.add(theaterList);
        }

        return theaterLists;
    }




    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto.seatTheaterResponse> searchSeatTheater(String theaterName){
        return theaterRepository.findByTheaterNameContaining(theaterName).stream()
                .map(TheaterDto.seatTheaterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheaterDto.seatTheaterResponse> getSeatTheater() {
        List<Theater> theaters = theaterRepository.findAll();
        List<TheaterDto.seatTheaterResponse> theaterLists = new ArrayList<>();
        for (Theater theater : theaters) {
            TheaterDto.seatTheaterResponse theaterResponse = new TheaterDto.seatTheaterResponse(theater);
            theaterLists.add(theaterResponse);
        }
        return theaterLists;
    }
}
